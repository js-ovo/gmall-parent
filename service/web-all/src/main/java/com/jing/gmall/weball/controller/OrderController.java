package com.jing.gmall.weball.controller;

import com.jing.gmall.feignclients.order.OrderFeignClient;
import com.jing.gmall.order.vo.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @GetMapping("/trade.html")
    public String orderPage(Model model){

        OrderConfirmVo confirmVo = orderFeignClient.getOrderConfirmData().getData();

        model.addAttribute("detailArrayList",confirmVo.getDetailArrayList());
        model.addAttribute("totalNum",confirmVo.getTotalNum());
        model.addAttribute("totalAmount",confirmVo.getTotalAmount());
        model.addAttribute("userAddressList",confirmVo.getUserAddressList());
        model.addAttribute("tradeNo",confirmVo.getTradeNo());
        return "order/trade";
    }

    @GetMapping("/myOrder.html")
    public String myOrderPage(){
        return "order/myOrder";
    }
}
