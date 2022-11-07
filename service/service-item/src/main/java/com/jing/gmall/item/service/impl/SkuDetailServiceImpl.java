package com.jing.gmall.item.service.impl;

import com.jing.gmall.item.feign.SkuFeignClient;
import com.jing.gmall.item.service.CacheService;
import com.jing.gmall.item.service.SkuDetailService;
import com.jing.gmall.item.vo.CategoryView;
import com.jing.gmall.item.vo.SkuDetailVo;
import com.jing.gmall.product.entity.SkuImage;
import com.jing.gmall.product.entity.SkuInfo;
import com.jing.gmall.product.entity.SpuSaleAttr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private CacheService cacheService;

    /**
     * 引入缓存查询 商品的详情信息
     * @param skuId
     * @return
     */
    @Override
    public SkuDetailVo getSkuDetail(Long skuId) {

        String cacheKey = "sku:info:" + skuId;
        //从缓存中查询数据
        SkuDetailVo cacheData = cacheService.getCacheData(cacheKey);
        //缓存命中 直接返回
        if (cacheData != null){
            return cacheData;
        }
        // 缓存未命中
        // 去bitMap中查找是否有该商品信息
        boolean isExist = cacheService.existSkuIdBitMap(skuId);
        // 有 -> 远程调用查询
        if (isExist){
            // 将数据存入 缓存 返回
            SkuDetailVo skuDetailVo = getSkuDetailVoFromRpc(skuId);
            cacheService.saveCache(cacheKey,skuDetailVo);
            return skuDetailVo;
        }
        // 没有 -> 返回null
       return null;
    }


    /**
     * 从远程服务查询数据库
     * 获取商品的详细信息
     * @param skuId
     * @return
     */
    private SkuDetailVo getSkuDetailVoFromRpc(Long skuId) {
        SkuDetailVo skuDetailVo = new SkuDetailVo();
        // 获取商品的基本信息
        SkuInfo skuInfo = skuFeignClient.getSkuInfo(skuId).getData();
        // 获取商品分类完整路径
        CategoryView categoryView = skuFeignClient.getCategoryView(skuInfo.getCategory3Id()).getData();
        // 获取商品的所有图片信息
        List<SkuImage> images = skuFeignClient.getSkuImages(skuId).getData();
        skuInfo.setSkuImageList(images);
        // 获取商品的所有销售属性
        List<SpuSaleAttr> spuSaleAttrs = skuFeignClient.getSpuSaleAttrAndValue(skuInfo.getSpuId(), skuId).getData();
        // 获取skuJson
        String json = skuFeignClient.getSkuJson(skuInfo.getSpuId()).getData();
        // 获取商品的实时价格
        BigDecimal price  = skuFeignClient.getRealtimePrice(skuId).getData();
        skuDetailVo.setSpuSaleAttrList(spuSaleAttrs);
        skuDetailVo.setSkuInfo(skuInfo);
        skuDetailVo.setCategoryView(categoryView);
        skuDetailVo.setValuesSkuJson(json);
        skuDetailVo.setPrice(price);
        return skuDetailVo;
    }
}
