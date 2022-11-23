package com.jing.gmall.payment.notify;


import com.alipay.api.AlipayApiException;
import com.jing.gmall.common.config.mq.service.MqService;
import com.jing.gmall.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payment/alipay")
@Slf4j
public class NotifyController {


    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MqService mqService;


    /**
     * 订单支付成功后接收支付宝发来的异步调用请求
     * @param params
     * @return
     */
    @PostMapping("/trade/notify")
    public String notifyMethod(Map<String,String> params) throws AlipayApiException {
        boolean signVerified  = paymentService.checkSign(params);
        log.info("签名信息:{}",params);
        if (signVerified){
            // 签名验证成功   消息队列发送消息,订单服务更新 订单状态
            //mqService.convertAndSend(MqConst.ORDER_EVENT_EXCHANGE,MqConst.ORDER_PAYED_RK,params);
            log.info("签名验证成功...向订单服务发送消息,修改订单状态");
        } else {
            log.error("签名验证失败！！！！！");
        }
        return "success";
    }
}
