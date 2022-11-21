package com.jing.gmall.order.service;

import com.jing.gmall.order.entity.OrderInfo;
import com.jing.gmall.order.vo.OrderConfirmVo;
import com.jing.gmall.order.vo.OrderSubmitVo;

/**
 * 处理订单 业务
 */
public interface OrderService {
    OrderConfirmVo getOrderConfirmData();

    Long submitOrder(String tradeNo, OrderSubmitVo orderSubmitVo);

    OrderInfo getOrderInfo(Long userId, Long orderId);
}
