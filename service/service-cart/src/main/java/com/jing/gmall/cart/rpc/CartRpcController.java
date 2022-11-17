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
        //  要存入的用户
        String key = cartService.resolveKey();
        //  存入商品
        SkuInfo skuInfo = cartService.addProductToCart(key,skuId,skuNum);
        return Result.ok(skuInfo);
    }


    @DeleteMapping("/deleteChecked")
    public Result deleteChecked(){
        String key = cartService.resolveKey();
        cartService.deleteChecked(key);
        return Result.ok();
    }
}
