package com.jing.gmall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jing.gmall.common.result.Result;
import com.jing.gmall.product.entity.BaseCategory1;
import com.jing.gmall.product.entity.BaseCategory2;
import com.jing.gmall.product.entity.BaseCategory3;
import com.jing.gmall.product.service.BaseCategory1Service;
import com.jing.gmall.product.service.BaseCategory2Service;
import com.jing.gmall.product.service.BaseCategory3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/getCategory3/{category2Id}")
    public Result getCateGory3(@PathVariable("category2Id") Long category2Id){
        QueryWrapper<BaseCategory3> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category2_id",category2Id);
        List<BaseCategory3> category3s = baseCategory3Service.list(queryWrapper);
        return Result.ok(category3s);
    }
}
