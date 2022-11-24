package com.jing.gmall.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderPayedMsg extends BaseMsg{

    @JsonProperty("gmt_create")
    private String gmtCreate;
    @JsonProperty("charset")
    private String charset;
    @JsonProperty("gmt_payment")
    private String gmtPayment;
    @JsonProperty("notify_time")
    private String notifyTime;
    @JsonProperty("subject")
    private String subject;
    @JsonProperty("sign")
    private String sign;
    @JsonProperty("buyer_id")
    private String buyerId;
    @JsonProperty("invoice_amount")
    private String invoiceAmount;
    @JsonProperty("version")
    private String version;
    @JsonProperty("notify_id")
    private String notifyId;
    @JsonProperty("fund_bill_list")
    private String fundBillList;
    @JsonProperty("notify_type")
    private String notifyType;
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    @JsonProperty("total_amount")
    private String totalAmount;
    @JsonProperty("trade_status")
    private String tradeStatus;
    @JsonProperty("trade_no")
    private String tradeNo;
    @JsonProperty("auth_app_id")
    private String authAppId;
    @JsonProperty("receipt_amount")
    private String receiptAmount;
    @JsonProperty("point_amount")
    private String pointAmount;
    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("buyer_pay_amount")
    private String buyerPayAmount;
    @JsonProperty("sign_type")
    private String signType;
    @JsonProperty("seller_id")
    private String sellerId;

}
