package com.jing.gmall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jing.gmall.common.result.Result;
import com.jing.gmall.product.entity.*;
import com.jing.gmall.product.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class CateGoryController {

    @Autowired
    private BaseCategory1Service baseCategory1Service;
    @Autowired
    private BaseCategory2Service baseCategory2Service;
    @Autowired
    private BaseCategory3Service baseCategory3Service;
    @Autowired
    private BaseAttrInfoService baseAttrInfoService;
    @Autowired
    private BaseAttrValueService baseAttrValueService;


    /**
     *  返回所有一级分类
     * @return
     */
    @GetMapping("/getCategory1")
    public Result getCateGory1(){
        List<BaseCategory1> category1s = baseCategory1Service.list();
        return Result.ok(category1s);
    }

    /**
     *  根据一级分类,返回所有二级分类
     * @param category1Id
     * @return
     */
    @GetMapping("/getCategory2/{category1Id}")
    public Result getCateGory2(@PathVariable("category1Id") Long category1Id){
        QueryWrapper<BaseCategory2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category1_id",category1Id);
        List<BaseCategory2> category2s = baseCategory2Service.list(queryWrapper);
        return Result.ok(category2s);
    }

    /**
     *  根据二级分类,返回所有三级分类
     * @param category2Id
     * @return
     */
    @GetMapping("/getCategory3/{category2Id}")
    public Result getCateGory3(@PathVariable("category2Id") Long category2Id){
        QueryWrapper<BaseCategory3> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category2_id",category2Id);
        List<BaseCategory3> category3s = baseCategory3Service.list(queryWrapper);
        return Result.ok(category3s);
    }


    /**
     * 返回商品属性信息和值
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result attrInfoList(@PathVariable("category1Id") Long category1Id,
                               @PathVariable("category2Id") Long category2Id,
                               @PathVariable("category3Id") Long category3Id){

        List<BaseAttrInfo> attrInfos = baseAttrInfoService.attrInfoList(category1Id,category2Id,category3Id);
        return Result.ok(attrInfos);
    }

    /**
     * 保存属性信息/修改属性信息
     * @param baseAttrInfo
     * @return
     */
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        baseAttrInfoService.saveAttrInfo(baseAttrInfo);
        return Result.ok();
    }

    /**
     *根据平台属性ID获取平台属性对象数据
     * @param attrId
     * @return
     */
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId") Long attrId){
        List<BaseAttrValue> attrValues = baseAttrValueService.getAttrValueList(attrId);
        return Result.ok(attrValues);
    }

}
