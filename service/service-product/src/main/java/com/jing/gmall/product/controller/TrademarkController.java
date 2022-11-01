package com.jing.gmall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jing.gmall.common.result.Result;
import com.jing.gmall.product.entity.BaseTrademark;
import com.jing.gmall.product.service.BaseTrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class TrademarkController {
    @Autowired
    private BaseTrademarkService baseTrademarkService;

    /**
     * 获取产品列表
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/baseTrademark/{page}/{limit}")
    public Result baseTrademarkList(@PathVariable("page") Long page,@PathVariable("limit") Long limit){
        Page<BaseTrademark> baseTrademarkPage = baseTrademarkService.page(new Page<>(page,limit));
        return Result.ok(baseTrademarkPage);
    }


    /**
     * 添加品牌信息
     * @param baseTrademark
     * @return
     */
    @PostMapping("/baseTrademark/save")
    public Result save(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    /**
     * 删除品牌信息
     * @param id
     * @return
     */
    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result delete(@PathVariable("id") Long id){
        baseTrademarkService.removeById(id);
        return Result.ok();
    }


    /**
     * 修改品牌信息
     * @param baseTrademark
     * @return
     */
    @PutMapping("/baseTrademark/update")
    public Result update(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }


    /**
     * 获取品牌信息
     * @param id
     * @return
     */
    @GetMapping("/baseTrademark/get/{id}")
    public Result getTrademark(@PathVariable("id") Long id){
        BaseTrademark trademark = baseTrademarkService.getById(id);
        return Result.ok(trademark);
    }

    /**
     * 获取所有的品牌列表
     * @return
     */
    @GetMapping("/baseTrademark/getTrademarkList")
    public Result getTrademarkList(){
        List<BaseTrademark> baseTrademarks = baseTrademarkService.list();
        return Result.ok(baseTrademarks);
    }


}
