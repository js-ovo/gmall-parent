package com.jing.gmall.common.constant;

/**
 * 声明RabbitMQ 交换机 队列 路由键
 */
public class MqConst {
    /**
     * 订单服务交换机
     */
    public static final String ORDER_EVENT_EXCHANGE = "order-event-exchange";

    /**
     * 订单延时队列
     */
    public static final String ORDER_DELAY_QUEUE = "order-delay-queue";
    /**
     * 订单创建路由键
     */
    public static final String ORDER_CREATE_RK = "order.create";
    /**
     * 订单死信队列 存放过期订单
     */
    public static final String ORDER_DEAD_QUEUE = "order-dead-queue";
    /**
     * 订单过期 绑定私信队列的 路由键
     */
    public static final String ORDER_TIMEOUT_RK = "order.timeout";

    /**
     * 订单支付成功队列
     */
    public static final String ORDER_PAYED_QUEUE = "order-payed-queue";
    /**
     * 订单 支付成功 路由键
     */
    public static final String ORDER_PAYED_RK = "order.payed";


    /**
     * 订单过期时间
     */
    public static final int ORDER_TTL_MS = 30 * 60 *1000;




}
