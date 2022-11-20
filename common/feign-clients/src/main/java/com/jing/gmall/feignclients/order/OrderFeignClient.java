package com.jing.gmall.feignclients.order;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.order.vo.OrderConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-order")
@RequestMapping("/api/inner/rpc/order")
public interface OrderFeignClient {
    @GetMapping("/confirmData")
    Result<OrderConfirmVo> getOrderConfirmData();
}
