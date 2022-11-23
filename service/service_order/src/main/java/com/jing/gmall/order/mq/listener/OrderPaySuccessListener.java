package com.jing.gmall.order.mq.listener;

import com.jing.gmall.common.config.mq.service.MqService;
import com.jing.gmall.msg.OrderPayedMsg;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderPaySuccessListener {

    @Autowired
    private MqService mqService;


//    @RabbitListener(queues = MqConst.ORDER_PAYED_QUEUE)
    public void orderPayedSuccess(Message message, Channel channel){
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            OrderPayedMsg payedMsg = mqService.getContent(message, OrderPayedMsg.class);
        } catch (Exception e){
            log.error("接收消息出错正在重试.....");
            mqService.retry(message,channel,3L);
        }

    }
}
