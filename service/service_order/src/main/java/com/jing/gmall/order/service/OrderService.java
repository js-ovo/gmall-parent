package com.jing.gmall.order.service;

import com.jing.gmall.order.entity.OrderInfo;
import com.jing.gmall.order.vo.OrderConfirmVo;
import com.jing.gmall.order.vo.OrderSplitVo;
import com.jing.gmall.order.vo.OrderSubmitVo;

import java.util.List;

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


    /**
     * 获取订单信息 根据订单号 可能会全库扫描
     * @param orderId
     * @return
     */
    OrderInfo getOrderInfo(Long orderId);

    /**
     * 支付成功 库存修改后 更新订单状态
     * @param id
     * @param userId
     * @param status 库存更改返回信息
     */
    void deduceOrder(Long id, Long userId, String status);


    /**
     * 将订单拆分
     * @param orderId 订单id
     * @param wareSkuMap 仓储系统传入的拆分参数
     * @return
     */
    List<OrderSplitVo> orderSplit(Long orderId, String wareSkuMap);

}
