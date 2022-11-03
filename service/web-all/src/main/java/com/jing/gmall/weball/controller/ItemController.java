package com.jing.gmall.weball.controller;

import com.jing.gmall.item.vo.SkuDetailVo;
import com.jing.gmall.weball.feign.SkuDetailFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商品详情
 */
@Controller
public class ItemController {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    /**
     * 查询商品详情
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String item(@PathVariable("skuId") Long skuId, Model model){

        SkuDetailVo skuDetailVo = skuDetailFeignClient.getSkuDetail(skuId).getData();

        // CategoryView
        model.addAttribute("categoryView",skuDetailVo.getCategoryView());
        // SkuInfo skuImgaeList
        model.addAttribute("skuInfo",skuDetailVo.getSkuInfo());
        // spuSaleAttrList
        model.addAttribute("spuSaleAttrList",skuDetailVo.getSpuSaleAttrList());
        // valuesSkuJson
        model.addAttribute("valuesSkuJson",skuDetailVo.getValuesSkuJson());
        return "item/index";
    }
}
