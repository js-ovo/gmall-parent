package com.jing.gmall.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName seckill_goods
 */
@TableName(value ="seckill_goods")
@Data
public class SeckillGoods implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * spu_id
     */
    @TableField(value = "spu_id")
    private Long spuId;

    /**
     * sku_id
     */
    @TableField(value = "sku_id")
    private Long skuId;

    /**
     * 标题
     */
    @TableField(value = "sku_name")
    private String skuName;

    /**
     * 商品图片
     */
    @TableField(value = "sku_default_img")
    private String skuDefaultImg;

    /**
     * 原价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 秒杀价格
     */
    @TableField(value = "cost_price")
    private BigDecimal costPrice;

    /**
     * 添加日期
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 审核日期
     */
    @TableField(value = "check_time")
    private Date checkTime;

    /**
     * 审核状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 秒杀商品数
     */
    @TableField(value = "num")
    private Integer num;

    /**
     * 剩余库存数
     */
    @TableField(value = "stock_count")
    private Integer stockCount;

    /**
     * 描述
     */
    @TableField(value = "sku_desc")
    private String skuDesc;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}