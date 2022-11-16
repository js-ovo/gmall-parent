package com.jing.gmall.cart.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车表 用户登录系统时更新冗余
 * @TableName cart_info
 */
@Data
public class CartInfo implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * skuid
     */
    private Long skuId;

    /**
     * 放入购物车时价格
     */
    private BigDecimal cartPrice;

    /**
     * 数量
     */
    private Integer skuNum;

    /**
     * 图片文件
     */
    private String imgUrl;


    private String skuName;

    private Integer isChecked;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}