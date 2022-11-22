package com.jing.gmall.order;

import com.jing.gmall.common.config.mq.service.MqService;
import com.jing.gmall.common.constant.MqConst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitTest {

    @Autowired
    private MqService mqService;

    @Test
    public void sendTest(){
        mqService.convertAndSend(MqConst.ORDER_EVENT_EXCHANGE,MqConst.ORDER_CREATE_RK,"123456");
        System.out.println("消息发送完成");
    }
}
