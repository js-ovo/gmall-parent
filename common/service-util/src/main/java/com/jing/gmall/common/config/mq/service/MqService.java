package com.jing.gmall.common.config.mq.service;

import com.jing.gmall.common.util.Jsons;
import com.jing.gmall.common.util.MD5;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MqService {

    private RabbitTemplate rabbitTemplate;
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 统计 消费消息失败的重试次数
     */
    private static final String MQ_RETRY_COUNT = "rabbit:msg:";

    public MqService(RabbitTemplate rabbitTemplate,StringRedisTemplate stringRedisTemplate){
        this.rabbitTemplate = rabbitTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        initTemplate();
    }



    public void initTemplate(){
        // 设置 生产者向交换机发送 消息的回调   消息发送到交换机  失败成功都会返回回调消息
        /** correlationData 消息的唯一关联id
         *  ack  消息的确认状态
         *  cause 消息失败的原因
         */
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            log.info("消息【Ack】: {},cause:【{}】,correlationData: 【{}】",ack,cause,correlationData);
        });
        // 生产者将消息发送到交换机,成功存入队列中后回调  失败会返回错误原因,成功不做任何返回
        /**Message message 发送的消息
         * replyCode 返回状态码
         * replyText 状态文本
         * exchange 交换机
         * routingKey 路由键
         */
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            // 消息失败回调
            log.error("消息【{}】发送存入队列失败 ,replyCode=【{}】,replyText=【{}】,exchange=【{}】,routingKey=【{}】",message,replyCode,replyText,exchange,routingKey);
            // 将失败消息存入数据库
            log.error("失败消息存入数据库。。。。");
        });
        // 设置重试器
        rabbitTemplate.setRetryTemplate(new RetryTemplate());
    }



    /**
     * 向rabbit中发送信息
     * @param exchange 交换机
     * @param routeKey 路由键
     * @param obj 消息
     */
    public void convertAndSend(String exchange,String routeKey,Object obj){

        String msg;

        if (obj instanceof String){
            msg = obj.toString();
        } else {
            msg = Jsons.toJsonStr(obj);
        }
        // 消息的唯一关联id
        CorrelationData cod = new CorrelationData(UUID.randomUUID().toString().replace("-",""));
        this.rabbitTemplate.convertAndSend(exchange,routeKey,msg,cod);
    }


    /**
     * 消费消息失败重试
     * @param message 消息
     * @param channel 通道
     * @param count 重试次数
     */
    public void retry(Message message, Channel channel, Long count){
        String json = new String(message.getBody());
        //消息内容一样 md5 加密后就一样
        String encrypt = MD5.encrypt(json);

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // 每重试一次 在redis中统计重试次数
        Long increment = stringRedisTemplate.opsForValue().increment(MQ_RETRY_COUNT + encrypt);
        if (increment > count){
            log.error("消息重试次数已经超过【{}】次,请手动处理.消息内容:{}",count,json);
        } else {
            // 消息回到队列
            try {
                channel.basicNack(deliveryTag,false,true);
                //自动 删除redis中的计数 设置一个过期时间  十分钟后自动过期    如果消费了  超过时间就自动删除
                // 在规定时间内 没有消费就会重置
                stringRedisTemplate.expire(MQ_RETRY_COUNT + encrypt,10, TimeUnit.MINUTES);
            } catch (IOException e) {
            }
        }

    }


    /**
     * 获取消息中的内容
     * @param message
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getContent(Message message,Class<T> type){
        String json = new String(message.getBody());
        return Jsons.json2Obj(json, type);
    }
}
