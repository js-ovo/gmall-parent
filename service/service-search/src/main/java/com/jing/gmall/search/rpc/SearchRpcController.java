package com.jing.gmall.search.rpc;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.search.entity.Goods;
import com.jing.gmall.search.service.SearchService;
import com.jing.gmall.search.vo.SearchParamVo;
import com.jing.gmall.search.vo.SearchRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inner/rpc/search")
public class SearchRpcController {

    @Autowired
    private SearchService searchService;


    /**
     * 搜索商品
     * @param searchParamVo
     * @return
     */
    @PostMapping("/goods")
    public Result<SearchRespVo> searchGoods(@RequestBody SearchParamVo searchParamVo){
        SearchRespVo searchRespVo = searchService.search(searchParamVo);
        return Result.ok(searchRespVo);
    }

    /**
     * 将商品保存到es
     * @param goods
     * @return
     */
    @PostMapping("/goods/save")
    public Result saveGoods(@RequestBody Goods goods){
        searchService.saveGoods(goods);
        return Result.ok();
    }


    /**
     * 从es中删除商品
     * @param skuId
     * @return
     */
    @DeleteMapping("/goods/delete/{skuId}")
    public Result deleteGoods(@PathVariable("skuId") Long skuId){
        searchService.deleteGoods(skuId);
        return Result.ok();
    }



}
