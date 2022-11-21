package com.jing.gmall.order.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 接收 提交订单时的参数信息
 */
@NoArgsConstructor
@Data
public class OrderSubmitVo {

    @JsonProperty("consignee")
    // 收件人
    private String consignee;
    @JsonProperty("consigneeTel")
    // 收件人联系方式
    private String consigneeTel;
    @JsonProperty("deliveryAddress")
    // 收件人联系地址
    private String deliveryAddress;
    // 订单备注
    @JsonProperty("orderComment")
    private String orderComment;
    // 购买的商品列表
    @JsonProperty("orderDetailList")
    private List<OrderDetailListDTO> orderDetailList;

    @NoArgsConstructor
    @Data
    public static class OrderDetailListDTO {
        @JsonProperty("skuId")
        private Long skuId;
        @JsonProperty("skuName")
        private String skuName;
        @JsonProperty("skuNum")
        private Integer skuNum;
        @JsonProperty("orderPrice")
        private BigDecimal orderPrice;
        @JsonProperty("imgUrl")
        private String imgUrl;
    }
}
