package com.jing.gmall.item.service.impl;

import com.jing.gmall.item.feign.SkuFeignClient;
import com.jing.gmall.item.service.SkuDetailService;
import com.jing.gmall.item.vo.CategoryView;
import com.jing.gmall.item.vo.SkuDetailVo;
import com.jing.gmall.product.entity.SkuImage;
import com.jing.gmall.product.entity.SkuInfo;
import com.jing.gmall.product.entity.SpuSaleAttr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    private SkuFeignClient skuFeignClient;
    /**
     * 获取商品的详细信息
     * @param skuId
     * @return
     */
    @Override
    public SkuDetailVo getSkuDetail(Long skuId) {
        SkuDetailVo skuDetailVo = new SkuDetailVo();
        // 获取商品的基本信息
        SkuInfo skuInfo = skuFeignClient.getSkuInfo(skuId).getData();
        // 获取商品分类完整路径
        CategoryView categoryView = skuFeignClient.getCategoryView(skuInfo.getCategory3Id()).getData();
        // 获取商品的所有图片信息
        List<SkuImage> images = skuFeignClient.getSkuImages(skuId).getData();
        skuInfo.setSkuImageList(images);
        // 获取商品的所有销售属性
        List<SpuSaleAttr> spuSaleAttrs = skuFeignClient.getSpuSaleAttrAndValue(skuInfo.getSpuId(),skuId).getData();
        // 获取skuJson
        String json = skuFeignClient.getSkuJson(skuInfo.getSpuId()).getData();
        skuDetailVo.setSpuSaleAttrList(spuSaleAttrs);
        skuDetailVo.setSkuInfo(skuInfo);
        skuDetailVo.setCategoryView(categoryView);
        skuDetailVo.setValuesSkuJson(json);
        return skuDetailVo;
    }
}
