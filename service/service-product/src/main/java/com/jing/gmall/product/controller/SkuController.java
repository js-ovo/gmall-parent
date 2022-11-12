package com.jing.gmall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jing.gmall.common.result.Result;
import com.jing.gmall.product.entity.SkuInfo;
import com.jing.gmall.product.service.SkuInfoService;
import com.jing.gmall.product.vo.SkuInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product")
public class SkuController {
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 获取spu分页列表
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/list/{page}/{limit}")
    public Result list(@PathVariable("page") Long page,@PathVariable("limit") Long limit){
        Page<SkuInfo> skuInfoPage = skuInfoService.page(new Page<>(page, limit));
        return Result.ok(skuInfoPage);
    }

    /**
     * 保存sku信息
     * @param skuInfoVo
     * @return
     */
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfoVo skuInfoVo){
        skuInfoService.saveSkuInfo(skuInfoVo);
        return Result.ok();
    }


    /**
     * 上架
     * @param skuId
     * @return
     */
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId){
        skuInfoService.onSale(skuId);
//        SkuInfo skuInfo = skuInfoService.getById(skuId);
//        skuInfo.setIsSale(1);
//        skuInfoService.updateById(skuInfo);
        return Result.ok();
    }

    /**
     * 下架
     * @param skuId
     * @return
     */
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId") Long skuId){
//        SkuInfo skuInfo = skuInfoService.getById(skuId);
//        skuInfo.setIsSale(0);
//        skuInfoService.updateById(skuInfo);

        skuInfoService.cancelSale(skuId);
        return Result.ok();
    }


    @DeleteMapping("/deleteSku/{skuId}")
    public Result deleteSku(@PathVariable("skuId") Long skuId){
        skuInfoService.removeSku(skuId);
        return Result.ok();
    }
}
