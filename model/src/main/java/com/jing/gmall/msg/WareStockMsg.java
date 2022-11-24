package com.jing.gmall.msg;

import lombok.Data;

import java.util.List;

@Data
public class WareStockMsg {
    private Long orderId;
    private String consignee;
    private String consigneeTel;
    private String orderComment;
    private String orderBody;
    private String deliveryAddress;
    private String paymentWay = "2";
    private List<Details> details;


    @Data
    public static class Details{
        private Long skuId;
        private String skuName;
        private Integer skuNum;

    }
}
