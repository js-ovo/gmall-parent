package com.jing.gmall.payment;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.jing.gmall.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PayTest {
    @Autowired
    AlipayClient alipayClient;
    @Autowired
    PaymentService paymentService;


    @Test
    void test() throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl("");
        request.setReturnUrl("");
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", "20210817010101004");
        bizContent.put("total_amount", 0.01);
        bizContent.put("subject", "测试商品");
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
//bizContent.put("time_expire", "2022-08-01 22:00:00");

//// 商品明细信息，按需传入
//JSONArray goodsDetail = new JSONArray();
//JSONObject goods1 = new JSONObject();
//goods1.put("goods_id", "goodsNo1");
//goods1.put("goods_name", "子商品1");
//goods1.put("quantity", 1);
//goods1.put("price", 0.01);
//goodsDetail.add(goods1);
//bizContent.put("goods_detail", goodsDetail);

//// 扩展信息，按需传入
//JSONObject extendParams = new JSONObject();
//extendParams.put("sys_service_provider_id", "2088511833207846");
//bizContent.put("extend_params", extendParams);

        request.setBizContent(bizContent.toString());
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        if(response.isSuccess()){
            System.out.println(response.getBody());
        } else {
            System.out.println("调用失败");
        }
    }

    @Test
    void test2() throws AlipayApiException {
        String payPage = paymentService.createPayPage(802196729708412929L);
        System.out.println(payPage);
    }
}
