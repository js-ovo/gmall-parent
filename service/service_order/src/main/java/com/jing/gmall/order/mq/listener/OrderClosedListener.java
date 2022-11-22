package com.jing.gmall.order.mq.listener;

import com.jing.gmall.common.config.mq.service.MqService;
import com.jing.gmall.common.constant.MqConst;
import com.jing.gmall.msg.OrderCreateMsg;
import com.jing.gmall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Log;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听队列关闭订单
 */
@Component
@Slf4j
public class OrderClosedListener {
    @Autowired
    private MqService mqService;

    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = MqConst.ORDER_DEAD_QUEUE)
    public void closedOrder(Message message, Channel channel){
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 获取消息的内容
            OrderCreateMsg content = mqService.getContent(message, OrderCreateMsg.class);
            // 关闭订单
            orderService.closeOrder(content.getUserId(),content.getOrderId());
            log.info("用户【{}】订单【{}】关闭完成",content.getUserId(),content.getOrderId());
            // 关闭成功后 手动确认
            channel.basicAck(deliveryTag,false);
        } catch (Exception e){
            // 关闭失败尝试重试
            mqService.retry(message,channel,3L);
        }
    }
}
