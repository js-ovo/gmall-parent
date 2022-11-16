package com.jing.gmall.weball.controller;

import com.jing.gmall.feignclients.cart.CartFeignClient;
import com.jing.gmall.product.entity.SkuInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@Slf4j
public class CartController {


    @Autowired
    private CartFeignClient cartFeignClient;

    public static Map<Thread, HttpServletRequest> threadInfo = new ConcurrentHashMap<>();

    @GetMapping("/cart.html")
    public String toCart(){

        return "cart/index";
    }


    @GetMapping("/addCart.html")
    public String addCart(@RequestParam("skuId") Long skuId,
                          @RequestParam("skuNum") Integer skuNum,
                          Model model,
                          HttpServletRequest request){
        log.info("请求信息放入map中");
        threadInfo.put(Thread.currentThread(),request);
        SkuInfo skuInfo = cartFeignClient.addToCart(skuId, skuNum).getData();
        log.info("移除请求信息");
        threadInfo.remove(Thread.currentThread());
        model.addAttribute("skuInfo",skuInfo);
        model.addAttribute("skuNum",skuNum);
        return "cart/addCart";
    }
}
