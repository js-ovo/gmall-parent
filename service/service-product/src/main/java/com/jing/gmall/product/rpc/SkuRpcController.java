package com.jing.gmall.product.rpc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jing.gmall.common.result.Result;
import com.jing.gmall.item.vo.CategoryView;
import com.jing.gmall.product.entity.SkuImage;
import com.jing.gmall.product.entity.SkuInfo;
import com.jing.gmall.product.entity.SpuSaleAttr;
import com.jing.gmall.product.service.BaseCategory1Service;
import com.jing.gmall.product.service.SkuImageService;
import com.jing.gmall.product.service.SkuInfoService;
import com.jing.gmall.product.service.SpuSaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 获取商品信息 提供其他服务调用
 */
@RestController
@RequestMapping("/api/inner/rpc/product")
public class SkuRpcController {

    @Autowired
    private BaseCategory1Service baseCategory1Service;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private SkuImageService skuImageService;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    /**
     * 获取某个商品对应的全部分类信息 包含一级 二级 三级
     * @param category3Id 分类的三级id
     * @return
     */
    @GetMapping("/skuInfo/categoryView/{category3Id}")
    public Result<CategoryView> getCategoryView(@PathVariable("category3Id") Long category3Id){
        CategoryView categoryView = baseCategory1Service.getCategoryView(category3Id);
        return Result.ok(categoryView);
    }

    /**
     * 查询某个商品的sku_info
     * @param skuId
     * @return
     */
    @GetMapping("/skuInfo/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId){
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        return Result.ok(skuInfo);
    }

    /**
     * 查询对应商品的所有图片
     * @return
     */
    @GetMapping("/skuInfo/images/{skuId}")
    public Result<List<SkuImage>> getSkuImages(@PathVariable("skuId") Long skuId){
        QueryWrapper<SkuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id",skuId);
        List<SkuImage> images = skuImageService.list(wrapper);
        return Result.ok(images);
    }

    /**
     * 查询某个sku对应的spu定义的所有销售属性名和值
     * @param spuId
     * @return
     */
    @GetMapping("/skuInfo/spuSaleAttrAndValue/{spuId}/{skuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrAndValue(@PathVariable("spuId") Long spuId
            ,@PathVariable("skuId") Long skuId){
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrService.getSpuSaleAttrAndValueListWithOrder(spuId,skuId);
        return Result.ok(spuSaleAttrList);
    }

    /**
     * 查询对应兄弟的 json字符串
     * @param spuId
     * @return
     */
    @GetMapping("/skuInfo/json/{spuId}")
    public Result<String> getSkuJson(@PathVariable("spuId") Long spuId){
        String json = spuSaleAttrService.getSkuAttrValueJson(spuId);
        return Result.ok(json);
    }
}
