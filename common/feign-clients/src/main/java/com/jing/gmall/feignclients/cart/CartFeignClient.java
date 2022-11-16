package com.jing.gmall.feignclients.cart;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.product.entity.SkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-cart")
@RequestMapping("/api/inner/rpc/cart")
public interface CartFeignClient {

    @GetMapping("/add/{skuId}")
    Result<SkuInfo> addToCart(@PathVariable("skuId") Long skuId,
                                     @RequestParam("skuNum") Integer skuNum);
}
