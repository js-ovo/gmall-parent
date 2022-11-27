package com.jing.gmall.seckill;

import com.jing.gmall.common.config.exception.annotation.EnableExceptionHandler;
import com.jing.gmall.common.config.mq.annotation.EnableRabbitMq;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringCloudApplication
@MapperScan("com.jing.gmall.seckill.mapper")
@EnableScheduling
@EnableExceptionHandler
@EnableRabbitMq
public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class,args);
    }
}
