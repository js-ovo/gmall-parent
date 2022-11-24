package com.jing.gmall.order.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 返回给库存系统 拆分订单结果
 */
@NoArgsConstructor
@Data
public class OrderSplitVo {

    @JsonProperty("orderBody")
    private String orderBody;
    @JsonProperty("consignee")
    private String consignee;
    @JsonProperty("orderComment")
    private String orderComment;
    // 传入时的仓库编号
    @JsonProperty("wareId")
    private Long wareId;
    @JsonProperty("orderId")
    private Long orderId;
    @JsonProperty("deliveryAddress")
    private String deliveryAddress;
    @JsonProperty("details")
    private List<DetailsDTO> details;
    @JsonProperty("paymentWay")
    private String paymentWay;

    @NoArgsConstructor
    @Data
    public static class DetailsDTO {
        @JsonProperty("skuName")
        private String skuName;
        @JsonProperty("skuId")
        private Long skuId;
        @JsonProperty("skuNum")
        private Integer skuNum;
    }
}
