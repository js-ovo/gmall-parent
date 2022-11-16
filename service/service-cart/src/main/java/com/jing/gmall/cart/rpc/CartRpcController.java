package com.jing.gmall.cart.rpc;

import com.jing.gmall.cart.service.CartService;
import com.jing.gmall.common.result.Result;
import com.jing.gmall.product.entity.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/inner/rpc/cart")
public class CartRpcController {

    @Autowired
    private CartService cartService;

    @GetMapping("/add/{skuId}")
    public Result<SkuInfo> addToCart(@PathVariable("skuId") Long skuId,
                                     @RequestParam("skuNum") Integer skuNum,
                                     HttpServletRequest request){



        return Result.ok();
    }
}
