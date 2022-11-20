package com.jing.gmall.order.controller;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.order.service.OrderService;
import com.jing.gmall.order.vo.OrderSubmitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order/auth")
public class OrderApiController {

    ///api/order/auth/submitOrder?tradeNo=GML_1668957846719_3
    @Autowired
    private OrderService orderService;

    /**
     * 提交订单
     * @param tradeNo
     * @return
     */
    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestParam("tradeNo") String tradeNo,
                              @RequestBody OrderSubmitVo orderSubmitVo){
        Long orderId = orderService.submitOrder(tradeNo,orderSubmitVo);
        return Result.ok(orderId.toString()); // Long超过js最大长度
    }
}
