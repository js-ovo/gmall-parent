package com.jing.gmall.payment;

import com.jing.gmall.common.config.exception.annotation.EnableExceptionHandler;
import com.jing.gmall.common.config.mq.annotation.EnableRabbitMq;
import com.jing.gmall.common.interceptor.EnableUserAuthFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients("com.jing.gmall.feignclients.order")
@EnableUserAuthFeignInterceptor
@EnableExceptionHandler
@EnableRabbitMq
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class,args);
    }
}
