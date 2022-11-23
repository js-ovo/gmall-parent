package com.jing.gmall.payment.alipay.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.alipay")
@Getter
@Setter
public class AlipayProperties{

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    private String notifyUrl;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    private String returnUrl;
    /**
     * 网关地址
     * 线上：https://openapi.alipay.com/gateway.do
     * 沙箱：https://openapi.alipaydev.com/gateway.do
     */
    private String serverUrl = "https://openapi.alipaydev.com/gateway.do";

    /**
     * 开放平台上创建的应用的ID
     */
    private String appId;

    /**
     * 报文格式，推荐：json
     */
    private String format = "json";

    /**
     * 字符串编码，推荐：utf-8
     */
    private String charset = "utf-8";

    /**
     * 签名算法类型，推荐：RSA2
     */
    private String signType = "RSA2";

    /**
     * 商户私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥字符串（公钥模式下设置，证书模式下无需设置）
     */
    private String alipayPublicKey;

}