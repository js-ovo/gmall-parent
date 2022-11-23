package com.jing.gmall.payment.controller;

import com.alipay.api.AlipayApiException;
import com.jing.gmall.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment/alipay")
public class PayApiController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/submit/{orderId}")
    public String payPage(@PathVariable ("orderId") Long orderId) throws AlipayApiException {
        return paymentService.createPayPage(orderId);
    }
}
