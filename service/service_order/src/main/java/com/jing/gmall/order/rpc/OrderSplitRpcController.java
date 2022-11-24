package com.jing.gmall.order.rpc;

import com.jing.gmall.order.service.OrderService;
import com.jing.gmall.order.vo.OrderSplitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 接收库存系统发送的请求
 */
@RestController
@RequestMapping("/api/order/")
public class OrderSplitRpcController {

    @Autowired
    private OrderService orderService;

    /**
     * 接收 库存系统发送来拆分订单的请求
     * @param orderId
     * @param wareSkuMap
     * @return
     */
    @PostMapping("/orderSplit")
    public List<OrderSplitVo> orderSplit(@RequestParam("orderId") Long orderId, @RequestParam("wareSkuMap") String wareSkuMap){
        return orderService.orderSplit(orderId,wareSkuMap);
    }
}
