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

    void closeOrder(Long userId, Long orderId);

    /**
     * 根据 outTradeNo获取订单信息  支付成功后 关闭订单使用
     * @param outTradeNo
     * @return
     */
    OrderInfo getOrderInfo(String outTradeNo);

    /**
     * 修改订单状态为已经支付
     * @param orderId
     * @param userId
     */
    void updatePayedOrder(Long orderId, Long userId);
}
