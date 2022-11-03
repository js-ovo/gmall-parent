package com.jing.gmall.weball;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@EnableDiscoveryClient
//@EnableCircuitBreaker
@SpringCloudApplication
@EnableFeignClients
public class WebAllApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebAllApplication.class,args);
    }
}
