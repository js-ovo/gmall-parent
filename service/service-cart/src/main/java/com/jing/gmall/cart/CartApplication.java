package com.jing.gmall.cart;

import com.jing.gmall.common.config.exception.annotation.EnableExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients("com.jing.gmall.feignclients.product")
@EnableExceptionHandler
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class,args);
    }
}
