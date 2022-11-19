package com.jing.gmall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 订单明细表
 * @TableName order_detail
 */
@TableName(value ="order_detail")
@Data
public class OrderDetail implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * sku_id
     */
    @TableField(value = "sku_id")
    private Long skuId;

    /**
     * sku名称（冗余)
     */
    @TableField(value = "sku_name")
    private String skuName;

    /**
     * 图片名称（冗余)
     */
    @TableField(value = "img_url")
    private String imgUrl;

    /**
     * 购买价格(下单时sku价格）
     */
    @TableField(value = "order_price")
    private Integer orderPrice;

    /**
     * 购买个数
     */
    @TableField(value = "sku_num")
    private String skuNum;

    /**
     * 操作时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 实际支付金额
     */
    @TableField(value = "split_total_amount")
    private BigDecimal splitTotalAmount;

    /**
     * 促销分摊金额
     */
    @TableField(value = "split_activity_amount")
    private BigDecimal splitActivityAmount;

    /**
     * 优惠券分摊金额
     */
    @TableField(value = "split_coupon_amount")
    private BigDecimal splitCouponAmount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}