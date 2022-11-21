package com.jing.gmall.feignclients.order;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.order.entity.OrderInfo;
import com.jing.gmall.order.vo.OrderConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-order")
@RequestMapping("/api/inner/rpc/order")
public interface OrderFeignClient {
    /**
     * 获取提交订单页数据
     * @return
     */
    @GetMapping("/confirmData")
    Result<OrderConfirmVo> getOrderConfirmData();

    /**
     * 获取订单详细信息
     * @param orderId
     * @return
     */
    @RequestMapping("/getOrderInfo")
    Result<OrderInfo> getOrderInfo(@RequestParam("orderId") Long orderId);
}
