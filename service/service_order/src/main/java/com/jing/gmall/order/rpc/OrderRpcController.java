package com.jing.gmall.order.rpc;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.order.vo.OrderConfirmVo;
import com.jing.gmall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inner/rpc/order")
public class OrderRpcController {

    @Autowired
    private OrderService orderService;


    /**
     * 获取订单提交页数据
     * @return
     */
    @GetMapping("/confirmData")
    public Result<OrderConfirmVo> getOrderConfirmData(){
        OrderConfirmVo orderConfirmVo = orderService.getOrderConfirmData();
        return Result.ok(orderConfirmVo);
    }

}
