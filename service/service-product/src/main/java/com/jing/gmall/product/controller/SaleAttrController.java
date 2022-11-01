package com.jing.gmall.product.controller;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.product.entity.BaseSaleAttr;
import com.jing.gmall.product.entity.SpuSaleAttr;
import com.jing.gmall.product.service.BaseSaleAttrService;
import com.jing.gmall.product.service.SpuSaleAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 销售属性
 */
@RequestMapping("/admin/product")
@RestController
public class SaleAttrController {
    @Autowired
    private BaseSaleAttrService baseSaleAttrService;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    /**
     * 获取所有的销售属性信息
     * @return
     */
    @GetMapping("/baseSaleAttrList")
    public Result baseSaleAttrList(){
        List<BaseSaleAttr> attrs = baseSaleAttrService.list();
        return Result.ok(attrs);
    }


    /**
     * 获取对应spu的销售属性信息
     * @param spuId
     * @return
     */
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable("spuId") Long spuId){
        List<SpuSaleAttr> spuSaleAttrs = spuSaleAttrService.getSpuSaleAttrList(spuId);
        return Result.ok(spuSaleAttrs);
    }
}
