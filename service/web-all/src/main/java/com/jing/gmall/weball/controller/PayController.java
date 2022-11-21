package com.jing.gmall.weball.controller;

import com.jing.gmall.feignclients.order.OrderFeignClient;
import com.jing.gmall.order.entity.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PayController {
    @Autowired
    private OrderFeignClient orderFeignClient;

    @GetMapping("/pay.html")
    public String paymentPage(@RequestParam("orderId") Long orderId, Model model){
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId).getData();
        model.addAttribute("orderInfo",orderInfo);
        return "payment/pay";
    }
}
