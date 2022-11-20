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
 * 支付信息表
 * @TableName payment_info
 */
@TableName(value ="payment_info")
@Data
public class PaymentInfo implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 对外业务编号
     */
    @TableField(value = "out_trade_no")
    private String outTradeNo;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 订单编号
     */
    @TableField(value = "order_id")
    private String orderId;

    /**
     * 支付类型（微信 支付宝）
     */
    @TableField(value = "payment_type")
    private String paymentType;

    /**
     * 交易编号
     */
    @TableField(value = "trade_no")
    private String tradeNo;

    /**
     * 支付金额
     */
    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 交易内容
     */
    @TableField(value = "subject")
    private String subject;

    /**
     * 支付状态
     */
    @TableField(value = "payment_status")
    private String paymentStatus;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 回调时间
     */
    @TableField(value = "callback_time")
    private Date callbackTime;

    /**
     * 回调信息
     */
    @TableField(value = "callback_content")
    private String callbackContent;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}