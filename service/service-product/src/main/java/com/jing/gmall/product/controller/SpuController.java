package com.jing.gmall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jing.gmall.common.result.Result;
import com.jing.gmall.product.entity.SpuImage;
import com.jing.gmall.product.entity.SpuInfo;
import com.jing.gmall.product.service.SpuImageService;
import com.jing.gmall.product.service.SpuInfoService;
import com.jing.gmall.product.vo.SpuInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/product")
@RestController
public class SpuController {

    @Autowired
    private SpuInfoService spuInfoService;

    @Autowired
    private SpuImageService spuImageService;


    /**
     *获取spu分页列表
     * @param page
     * @param limit
     * @param c3Id
     * @return
     */
    @GetMapping("/{page}/{limit}")
    public Result getSpuInfoList(@PathVariable("page") Long page,
                                 @PathVariable("limit") Long limit,
                                 @RequestParam("category3Id") Long c3Id){

        QueryWrapper<SpuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("category3_id",c3Id);
        Page<SpuInfo> spuInfoPage = spuInfoService.page(new Page<>(page, limit), wrapper);
        return Result.ok(spuInfoPage);
    }


    /**
     * 获取对应spu的品牌图片
     * @param spuId
     * @return
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result getSpuImageList(@PathVariable("spuId") Long spuId){
        QueryWrapper<SpuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id",spuId);
        List<SpuImage> spuImages = spuImageService.list(wrapper);
        return Result.ok(spuImages);
    }

    /**
     * 保存Spu
     * @return
     */
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfoVo spuInfoVo){
        spuInfoService.saveSpuInfo(spuInfoVo);
        return Result.ok();
    }


}
