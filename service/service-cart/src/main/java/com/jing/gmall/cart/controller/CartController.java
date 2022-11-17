package com.jing.gmall.cart.controller;

import com.jing.gmall.cart.entity.CartInfo;
import com.jing.gmall.cart.service.CartService;
import com.jing.gmall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 获取当前用户的购物车列表
     * @return
     */
    @GetMapping("/cartList")
    public Result cartList(HttpServletRequest request){
        // 决定是 临时用户还是 登录
        String key = cartService.resolveKey();

        // 获取 购物车列表
        List<CartInfo> cartInfoList = cartService.getCartList(key);

        return  Result.ok(cartInfoList);
    }


    /**
     *  更新购物车中商品的数量
     * @param skuId
     * @param num
     * @return
     */
    @PostMapping("/addToCart/{skuId}/{num}")
    public Result addToCart(@PathVariable("skuId") Long skuId,
                            @PathVariable("num") Integer num){

        String key = cartService.resolveKey();
        cartService.updateProductCount(key,skuId,num);

        return Result.ok();
    }

    /**
     * 更新商品的选中状态
     * @param skuId
     * @param status
     * @return
     */
    @GetMapping("/checkCart/{skuId}/{status}")
    public Result updateCheckStatus(@PathVariable("skuId") Long skuId,
                                    @PathVariable("status") Integer status){

        String key = cartService.resolveKey();
        cartService.updateCheckStatus(key,skuId,status);
        return Result.ok();
    }


    /**
     * 移除购物车中指定商品
     * @param skuId
     * @return
     */
    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable("skuId") Long skuId){

        String key = cartService.resolveKey();
        cartService.removeProduct(key,skuId);
        return Result.ok();
    }



}
