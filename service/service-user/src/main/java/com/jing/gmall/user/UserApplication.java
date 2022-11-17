package com.jing.gmall.user;

import com.jing.gmall.common.config.exception.annotation.EnableExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
@MapperScan(basePackages = "com.jing.gmall.user.mapper")
@EnableExceptionHandler
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
