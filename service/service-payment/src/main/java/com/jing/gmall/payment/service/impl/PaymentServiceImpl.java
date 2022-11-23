package com.jing.gmall.payment.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.jing.gmall.common.util.Jsons;
import com.jing.gmall.feignclients.order.OrderFeignClient;
import com.jing.gmall.order.entity.OrderInfo;
import com.jing.gmall.payment.alipay.config.AlipayProperties;
import com.jing.gmall.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {


    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private AlipayProperties alipayProperties;
    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 根据订单id生成订单支付页面
     * @param orderId
     * @return
     */
    @Override
    public String createPayPage(Long orderId) throws AlipayApiException {
        // 导入alipay客户端
        // 设置请求参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        // 设置支付成功后浏览器同步跳转的页面
        request.setReturnUrl(alipayProperties.getReturnUrl());
        // 支付成功 支付宝异步发送请求地址    需要有外网
        request.setNotifyUrl(alipayProperties.getNotifyUrl());
        //获取订单信息 远程调用订单服务
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId).getData();
        log.info("订单数据为=>>>>>>>>>>:{}",orderInfo);
        // 封装其他请求参数
        Map<String,String> params = new HashMap<>();
        // 订单的流水号
        String out_trade_no = orderInfo.getOutTradeNo();
        // 订单金额
        String total_amount = orderInfo.getTotalAmount().toString();
        // 销售产品码
        String product_code = "FAST_INSTANT_TRADE_PAY";
        // 订单标题
        String subject = "尚品汇-" + orderInfo.getOutTradeNo();
        params.put("out_trade_no",out_trade_no);
        params.put("total_amount",total_amount);
        params.put("product_code",product_code);
        params.put("subject",subject);
        String jsonStr = Jsons.toJsonStr(params);
        log.info("请求参数数据:{}",jsonStr);
        request.setBizContent(jsonStr);
        return alipayClient.pageExecute(request).getBody();
    }


    /**
     * 验证支付成功后  异步调用返回参数
     * @param params
     * @return
     */
    @Override
    public boolean checkSign(Map<String, String> params) throws AlipayApiException {
        return AlipaySignature.rsaCheckV1(params,
                alipayProperties.getAlipayPublicKey(),
                alipayProperties.getCharset(),
                alipayProperties.getSignType());
    }
}
