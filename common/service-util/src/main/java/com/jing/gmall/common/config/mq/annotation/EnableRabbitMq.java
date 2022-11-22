package com.jing.gmall.common.config.mq.annotation;

import com.jing.gmall.common.config.mq.RabbitMqConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(RabbitMqConfiguration.class)
public @interface EnableRabbitMq {
}
