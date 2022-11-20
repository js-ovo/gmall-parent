package com.jing.gmall.feignclients.cart;

import com.jing.gmall.cart.entity.CartInfo;
import com.jing.gmall.common.result.Result;
import com.jing.gmall.product.entity.SkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("service-cart")
@RequestMapping("/api/inner/rpc/cart")
public interface CartFeignClient {

    @GetMapping("/add/{skuId}")
    Result<SkuInfo> addToCart(@PathVariable("skuId") Long skuId,
                                     @RequestParam("skuNum") Integer skuNum);

    @DeleteMapping("/deleteChecked")
    Result deleteChecked();

    @GetMapping("/getCheckedCartInfo")
    Result<List<CartInfo>> getCheckedCartInfo();
}
