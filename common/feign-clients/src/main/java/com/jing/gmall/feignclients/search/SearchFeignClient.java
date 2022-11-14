package com.jing.gmall.feignclients.search;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.search.entity.Goods;
import com.jing.gmall.search.vo.SearchParamVo;
import com.jing.gmall.search.vo.SearchRespVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-search")
@RequestMapping("/api/inner/rpc/search")
public interface SearchFeignClient {


    /**
     * 搜索商品
     * @param searchParamVo
     * @return
     */
    @PostMapping("/goods")
    Result<SearchRespVo> searchGoods(@RequestBody SearchParamVo searchParamVo);

    /**
     * 将商品保存到es
     * @param goods
     * @return
     */
    @PostMapping("/goods/save")
    Result saveGoods(@RequestBody Goods goods);


    /**
     * 从es中删除商品
     * @param skuId
     * @return
     */
    @DeleteMapping("/goods/delete/{skuId}")
    Result deleteGoods(@PathVariable("skuId") Long skuId);

    /**
     * 更新es中商品的 热度评分
     * @param skuId
     * @param hotScore
     * @return
     */
    @PutMapping("/goods/updateHotScore/{skuId}/{hotScore}")
    Result updateHotScore(@PathVariable("skuId") Long skuId,
                                 @PathVariable("hotScore") Long hotScore);

}
