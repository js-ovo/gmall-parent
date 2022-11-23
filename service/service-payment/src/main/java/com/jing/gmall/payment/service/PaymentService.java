package com.jing.gmall.payment.service;

import com.alipay.api.AlipayApiException;

import java.util.Map;

public interface PaymentService {
    String createPayPage(Long orderId) throws AlipayApiException;

    boolean checkSign(Map<String, String> params) throws AlipayApiException;
}
