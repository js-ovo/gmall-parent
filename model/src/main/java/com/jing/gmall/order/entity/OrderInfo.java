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
 * 订单表 订单表
 * @TableName order_info
 */
@TableName(value ="order_info")
@Data
public class OrderInfo implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 收货人
     */
    @TableField(value = "consignee")
    private String consignee;

    /**
     * 收件人电话
     */
    @TableField(value = "consignee_tel")
    private String consigneeTel;

    /**
     * 总金额
     */
    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    @TableField(value = "order_status")
    private String orderStatus;

    /**
     * 付款方式
     */
    @TableField(value = "payment_way")
    private String paymentWay;

    /**
     * 送货地址
     */
    @TableField(value = "delivery_address")
    private String deliveryAddress;

    /**
     * 订单备注
     */
    @TableField(value = "order_comment")
    private String orderComment;

    /**
     * 订单交易编号（第三方支付用)
     */
    @TableField(value = "out_trade_no")
    private String outTradeNo;

    /**
     * 订单描述(第三方支付用)
     */
    @TableField(value = "trade_body")
    private String tradeBody;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 失效时间
     */
    @TableField(value = "expire_time")
    private Date expireTime;

    /**
     * 进度状态
     */
    @TableField(value = "process_status")
    private String processStatus;

    /**
     * 物流单编号
     */
    @TableField(value = "tracking_no")
    private String trackingNo;

    /**
     * 父订单编号
     */
    @TableField(value = "parent_order_id")
    private Long parentOrderId;

    /**
     * 图片路径
     */
    @TableField(value = "img_url")
    private String imgUrl;

    /**
     * 省id
     */
    @TableField(value = "province_id")
    private Long provinceId;

    /**
     * 操作时间
     */
    @TableField(value = "operate_time")
    private Date operateTime;

    /**
     * 促销金额
     */
    @TableField(value = "activity_reduce_amount")
    private BigDecimal activityReduceAmount;

    /**
     * 优惠券金额
     */
    @TableField(value = "coupon_amount")
    private BigDecimal couponAmount;

    /**
     * 原价金额
     */
    @TableField(value = "original_total_amount")
    private BigDecimal originalTotalAmount;

    /**
     * 运费
     */
    @TableField(value = "feight_fee")
    private BigDecimal feightFee;

    /**
     * 可退款日期（签收后30天）
     */
    @TableField(value = "refundable_time")
    private Date refundableTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}