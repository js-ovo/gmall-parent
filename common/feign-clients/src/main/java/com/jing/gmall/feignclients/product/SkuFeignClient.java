package com.jing.gmall.feignclients.product;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.item.vo.CategoryView;
import com.jing.gmall.product.entity.SkuImage;
import com.jing.gmall.product.entity.SkuInfo;
import com.jing.gmall.product.entity.SpuSaleAttr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@FeignClient("service-product")
@RequestMapping("/api/inner/rpc/product")
public interface SkuFeignClient {
    /**
     * 获取某个商品对应的全部分类信息 包含一级 二级 三级
     * @param category3Id 分类的三级id
     * @return
     */
    @GetMapping("/skuInfo/categoryView/{category3Id}")
    Result<CategoryView> getCategoryView(@PathVariable("category3Id") Long category3Id);

    /**
     * 查询某个商品的sku_info
     * @param skuId
     * @return
     */
    @GetMapping("/skuInfo/{skuId}")
    Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId);

    /**
     * 查询对应商品的所有图片
     * @return
     */
    @GetMapping("/skuInfo/images/{skuId}")
    Result<List<SkuImage>> getSkuImages(@PathVariable("skuId") Long skuId);

    /**
     * 查询某个sku对应的spu定义的所有销售属性名和值
     * @param spuId
     * @return
     */
    @GetMapping("/skuInfo/spuSaleAttrAndValue/{spuId}/{skuId}")
    Result<List<SpuSaleAttr>> getSpuSaleAttrAndValue(@PathVariable("spuId") Long spuId
            ,@PathVariable("skuId") Long skuId);
    /**
     * 查询对应兄弟的 json字符串
     * @param spuId
     * @return
     */
    @GetMapping("/skuInfo/json/{spuId}")
    Result<String> getSkuJson(@PathVariable("spuId") Long spuId);

    /**
     * 查询对应商品的实时价格
     * @param skuId
     * @return
     */
    @GetMapping("/skuInfo/price/{skuId}")
    Result<BigDecimal> getRealtimePrice(@PathVariable("skuId") Long skuId);
}
