package com.jing.gmall.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName order_status_log
 */
@TableName(value ="order_status_log")
@Data
public class OrderStatusLog implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 订单id
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 订单状态
     */
    @TableField(value = "order_status")
    private String orderStatus;

    /**
     * 修改时间
     */
    @TableField(value = "operate_time")
    private Date operateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}