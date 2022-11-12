package com.jing.gmall.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;


//@SpringBootApplication
//@EnableDiscoveryClient // 开启服务注册与发现
//@EnableCircuitBreaker // 开启服务熔断 [hystrix,sentinel]
@SpringCloudApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
