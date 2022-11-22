package com.jing.gmall.common.config.mq;

import com.jing.gmall.common.config.mq.service.MqService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableRabbit // 开启注解版rabbit mq
@AutoConfigureAfter(RabbitAutoConfiguration.class)
@Import(MqService.class)
public class RabbitMqConfiguration {
}
