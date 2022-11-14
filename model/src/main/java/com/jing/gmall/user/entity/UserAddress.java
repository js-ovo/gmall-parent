package com.jing.gmall.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户地址表
 * @TableName user_address
 */
@TableName(value ="user_address")
@Data
public class UserAddress implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户地址
     */
    @TableField(value = "user_address")
    private String userAddress;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 收件人
     */
    @TableField(value = "consignee")
    private String consignee;

    /**
     * 联系方式
     */
    @TableField(value = "phone_num")
    private String phoneNum;

    /**
     * 是否是默认
     */
    @TableField(value = "is_default")
    private String isDefault;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}