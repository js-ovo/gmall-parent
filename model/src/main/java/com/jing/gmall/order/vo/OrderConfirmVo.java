package com.jing.gmall.order.vo;

import com.jing.gmall.user.entity.UserAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 返回跳转至订单确认页封装信息
 */
@Data
public class OrderConfirmVo {

    /**
     * 商品 信息列表
     */
    private List<Product> detailArrayList;

    /**
     * 订单总的商品数量
     */
    private Integer totalNum;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 收货信息 列表
     */
    private List<UserAddress> userAddressList;


    /**
     * 订单流水号 唯一
     */
    private String tradeNo;



    @Data
    public static class Product {
        private Long skuId;
        private String skuName;
        private Integer skuNum;
        //商品实际价格
        private BigDecimal orderPrice;
        private String imgUrl;
        // 商品是否有库存
        private String hasStock;
    }
}
