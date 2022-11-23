package com.jing.gmall.order.mq;

import com.jing.gmall.common.constant.MqConst;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 声明订单微服务的 队列交换机 绑定关系
 */
@Configuration
public class OrderMqConfiguration {


    /**
     * 订单微服务交换机
     */
    @Bean
    public Exchange orderEventExchange(){

        /**
         * durable:是否开启持久化
         * autoDelete:是否自动删除交换机
         * arguments:其他参数
         */
//        TopicExchange exchange =
//                new TopicExchange(MqConst.ORDER_EVENT_EXCHANGE,true,false,null);
        return ExchangeBuilder
                .topicExchange(MqConst.ORDER_EVENT_EXCHANGE).build();
    }


    /**
     * 订单延时队列
     */
    @Bean
    public Queue orderDelayQueue(){

        /**
         * String name, 队列名
         * boolean durable, 持久化
         * boolean exclusive, 排他; 如果消息消费需要顺序，可以排他。
         * boolean autoDelete, 自动删除
         * @Nullable Map<String, Object> arguments 参数
         */
//        Map<String, Object> arguments = new HashMap<>();
//        arguments.put("x-dead-letter-exchange",MqConst.ORDER_EVENT_EXCHANGE);
//        arguments.put("x-dead-letter-routing-key",MqConst.ORDER_TIMEOUT_RK);
//        arguments.put("x-message-ttl",MqConst.ORDER_TTL_MS); //消息的过期时间
//
//        Queue queue = new Queue(MqConst.ORDER_DELAY_QUEUE,
//                true,false,false,arguments);

        return QueueBuilder.durable(MqConst.ORDER_DELAY_QUEUE) // 队列名称
                .deadLetterExchange(MqConst.ORDER_EVENT_EXCHANGE) // 绑定的死信交换机
                .deadLetterRoutingKey(MqConst.ORDER_TIMEOUT_RK) // 死信路由键
                .ttl(MqConst.ORDER_TTL_MS)// 队列的过期时间
                .build();
    }

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue deadQueue(){
        return QueueBuilder.durable(MqConst.ORDER_DEAD_QUEUE).build();
    }

    /**
     * 订单支付成功队列
     * @return
     */
    @Bean
    public Queue paySuccessQueue(){
        return QueueBuilder.durable(MqConst.ORDER_PAYED_QUEUE).build();
    }




    //=============================声明绑定关系========================================


    @Bean
    public Binding delayBinding(){
        return new Binding(MqConst.ORDER_DELAY_QUEUE, // 绑定目标名称
                Binding.DestinationType.QUEUE, // 绑定类型  交换机 队列
                MqConst.ORDER_EVENT_EXCHANGE, // 需要绑定的交换机
                MqConst.ORDER_CREATE_RK, // 绑定的路由key
                null); // 参数
    }


    @Bean
    public Binding deadBinding(){
        return new Binding(MqConst.ORDER_DEAD_QUEUE, // 绑定目标名称
                Binding.DestinationType.QUEUE, // 绑定类型  交换机 队列
                MqConst.ORDER_EVENT_EXCHANGE, // 需要绑定的交换机
                MqConst.ORDER_TIMEOUT_RK, // 绑定的路由key
                null);
    }

    @Bean
    public Binding paySuccessBinding(){
        return new Binding(MqConst.ORDER_PAYED_QUEUE,
                Binding.DestinationType.QUEUE,
                MqConst.ORDER_EVENT_EXCHANGE,
                MqConst.ORDER_PAYED_RK,
                null);
    }


}
