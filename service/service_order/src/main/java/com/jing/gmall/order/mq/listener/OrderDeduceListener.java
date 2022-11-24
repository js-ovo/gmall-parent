package com.jing.gmall.order.mq.listener;

import com.jing.gmall.common.config.mq.service.MqService;
import com.jing.gmall.msg.WareOrderMsg;
import com.jing.gmall.order.entity.OrderInfo;
import com.jing.gmall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用来监听库存系统减完库存后的返回结果
 */
@Component
@Slf4j
public class OrderDeduceListener {

    @Autowired
    private MqService mqService;

    @Autowired
    private OrderService orderService;


    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "queue.ware.order",durable = "true",autoDelete = "false",exclusive = "false"),
                    exchange = @Exchange(value = "exchange.direct.ware.order",type = "direct",durable = "true",autoDelete = "false"),
            key = "ware.order")})
    public void orderDeduceListener(Message message, Channel channel){
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            WareOrderMsg content = mqService.getContent(message, WareOrderMsg.class);
            // 获取订单信息
            OrderInfo orderInfo = orderService.getOrderInfo(content.getOrderId());
            // 更新订单库存修改状态
            orderService.deduceOrder(orderInfo.getId(),orderInfo.getUserId(),content.getStatus());
            channel.basicAck(deliveryTag,false);
        }catch (Exception e){
            log.error("修改库存后更新订单状态失败 尝试重试中。。。。。");
            mqService.retry(message,channel,3L);
        }
    }


}
