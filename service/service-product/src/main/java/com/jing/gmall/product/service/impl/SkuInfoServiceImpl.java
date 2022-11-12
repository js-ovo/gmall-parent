package com.jing.gmall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.cache.servie.CacheService;
import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.feignclients.search.SearchFeignClient;
import com.jing.gmall.item.vo.CategoryView;
import com.jing.gmall.product.entity.*;
import com.jing.gmall.product.mapper.SkuInfoMapper;
import com.jing.gmall.product.service.*;
import com.jing.gmall.product.vo.SkuInfoVo;
import com.jing.gmall.search.entity.Goods;
import com.jing.gmall.search.entity.SearchAttr;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
* @author Jing
* @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
* @createDate 2022-10-30 18:14:38
*/
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{

    @Autowired
    private SkuImageService skuImageService;

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SearchFeignClient searchFeignClient;

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    @Autowired
    private BaseCategory1Service baseCategory1Service;

    @Autowired
    private ThreadPoolExecutor poolExecutor;

    @Transactional
    @Override
    public void saveSkuInfo(SkuInfoVo skuInfoVo) {
        // 保存 skuInfo到 sku_info表中
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo,skuInfo);
        this.save(skuInfo);

        // 返回skuId
        Long skuId = skuInfo.getId();
        // 保存 skuImage到 sku_image表中
        List<SkuInfoVo.SkuImageListDTO> skuImageDtoList = skuInfoVo.getSkuImageList();
        List<SkuImage> skuImages = skuImageDtoList.stream().map(skuImageDto -> {
            SkuImage skuImage = new SkuImage();
            BeanUtils.copyProperties(skuImageDto, skuImage);
            skuImage.setSkuId(skuId);
            return skuImage;
        }).collect(Collectors.toList());
        skuImageService.saveBatch(skuImages);

        // 保存 skuAttrValue 到 sku_attr_value表中
        List<SkuInfoVo.SkuAttrValueListDTO> skuAttrValueDtoList = skuInfoVo.getSkuAttrValueList();
        List<SkuAttrValue> skuAttrValues = skuAttrValueDtoList.stream().map(skuAttrValueDTO -> {
            SkuAttrValue skuAttrValue = new SkuAttrValue();
            BeanUtils.copyProperties(skuAttrValueDTO, skuAttrValue);
            skuAttrValue.setSkuId(skuId);
            return skuAttrValue;
        }).collect(Collectors.toList());
        skuAttrValueService.saveBatch(skuAttrValues);
        // 保存 skuSaleAttrValue 到 sku_sale_attr_value表中
        List<SkuInfoVo.SkuSaleAttrValueListDTO> skuSaleAttrValueDtoList = skuInfoVo.getSkuSaleAttrValueList();
        List<SkuSaleAttrValue> skuSaleAttrValues = skuSaleAttrValueDtoList.stream().map(skuSaleAttrValueDto -> {
            SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
            BeanUtils.copyProperties(skuSaleAttrValueDto, skuSaleAttrValue);
            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValue.setSpuId(skuInfoVo.getSpuId());
            return skuSaleAttrValue;
        }).collect(Collectors.toList());
        skuSaleAttrValueService.saveBatch(skuSaleAttrValues);
        //TODO 缓存bitmap中也存入
        stringRedisTemplate.opsForValue().setBit(RedisConst.SKUID_BITMAP_KEY,skuId,true);

    }

    @Override
    public BigDecimal getRealtimePrice(Long skuId) {
        return baseMapper.getRealtimePrice(skuId);
    }

    @Override
    public List<Long> getSkuIds() {
        return this.baseMapper.getSkuIds();
    }

    @Override
    public void removeSku(Long skuId) {
        // 删除数据库信息
        removeById(skuId);
        // 删除其他与之关联的信息

        cacheService.delayedDoubleDel(RedisConst.SKU_INFO_CACHE_KEY + skuId);

        //TODO 删除bitmap中
        stringRedisTemplate.opsForValue().setBit(RedisConst.SKUID_BITMAP_KEY,skuId,false);
    }

    /**
     * 上架
     * @param skuId
     */
    @Override
    public void onSale(Long skuId) {
        // 修改状态
        updateSkuSaleStatus(skuId,1);
        // 向es中添加数据
        // 封装Goods
        Goods goods = prepareGoods(skuId);
        searchFeignClient.saveGoods(goods);

    }


    /**
     * 将以sku信息封装成Goods存入 es
     * @param skuId
     * @return
     */
    private Goods prepareGoods(Long skuId) {
        Goods goods = new Goods();

        CompletableFuture<SkuInfo> skuInfoAsync = CompletableFuture.supplyAsync(() -> getById(skuId), poolExecutor);

        CompletableFuture<Void> trademarkAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {
            goods.setId(skuInfo.getId());
            goods.setDefaultImg(skuInfo.getSkuDefaultImg());
            goods.setTitle(skuInfo.getSkuName());
            goods.setPrice(skuInfo.getPrice().doubleValue());
            goods.setCreateTime(new Date());
            //TODO 查询品牌
            BaseTrademark trademark = baseTrademarkService.getById(skuInfo.getTmId());
            goods.setTmId(skuInfo.getTmId());
            goods.setTmName(trademark.getTmName());
            goods.setTmLogoUrl(trademark.getLogoUrl());
        }, poolExecutor);

        //TODO 商品三级分类
        CompletableFuture<Void> categoryViewAsync = CompletableFuture.runAsync(() -> {
            CategoryView categoryView = baseCategory1Service.getCategoryView(skuId);
            goods.setCategory1Id(categoryView.getCategory1Id());
            goods.setCategory1Name(categoryView.getCategory1Name());
            goods.setCategory2Id(categoryView.getCategory2Id());
            goods.setCategory2Name(categoryView.getCategory2Name());
            goods.setCategory3Id(categoryView.getCategory3Id());
            goods.setCategory3Name(categoryView.getCategory3Name());
        },poolExecutor);

        goods.setHotScore(0L);

        //TODO  属性

        CompletableFuture<Void> attrsAsync = CompletableFuture.runAsync(() -> {
            List<SearchAttr> attrs = skuAttrValueService.getSkuAttrNameAndValue(skuId);
            goods.setAttrs(attrs);
        }, poolExecutor);

        CompletableFuture.allOf(trademarkAsync,categoryViewAsync,attrsAsync).join();

        return goods;
    }


    /**
     * 下架
     * @param skuId
     */
    @Override
    public void cancelSale(Long skuId) {
        // 修改状态
        updateSkuSaleStatus(skuId,0);
        // 从es中删除数据
        searchFeignClient.deleteGoods(skuId);
    }


    private void updateSkuSaleStatus(Long skuId,Integer status){
        baseMapper.updateSkuSaleStatus(skuId,status);
    }
}




