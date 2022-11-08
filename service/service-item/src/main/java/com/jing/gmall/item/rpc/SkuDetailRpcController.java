package com.jing.gmall.item.rpc;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.item.service.SkuDetailService;
import com.jing.gmall.item.vo.SkuDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品详情
 */
@RestController
@RequestMapping("/api/inner/rpc/item")
public class SkuDetailRpcController {

    @Autowired
    private SkuDetailService skuDetailService;

    /**
     * 获取商品详情信息
     * @param skuId
     * @return
     */
    @GetMapping("/product/{skuId}")
    public Result<SkuDetailVo> getSkuDetail(@PathVariable("skuId") Long skuId){
        SkuDetailVo skuDetailVo = skuDetailService.getSkuDetail(skuId);
        return Result.ok(skuDetailVo);
    }

}
