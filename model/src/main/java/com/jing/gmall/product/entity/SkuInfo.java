package com.jing.gmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存单元表
 * @TableName sku_info
 */
@TableName(value ="sku_info")
@Data
public class SkuInfo implements Serializable {
    /**
     * 库存id(itemID)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    @TableField(value = "spu_id")
    private Long spuId;

    /**
     * 价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * sku名称
     */
    @TableField(value = "sku_name")
    private String skuName;

    /**
     * 商品规格描述
     */
    @TableField(value = "sku_desc")
    private String skuDesc;

    /**
     * 重量
     */
    @TableField(value = "weight")
    private BigDecimal weight;

    /**
     * 品牌(冗余)
     */
    @TableField(value = "tm_id")
    private Long tmId;

    /**
     * 三级分类id（冗余)
     */
    @TableField(value = "category3_id")
    private Long category3Id;

    /**
     * 默认显示图片(冗余)
     */
    @TableField(value = "sku_default_img")
    private String skuDefaultImg;

    /**
     * 是否销售（1：是 0：否）
     */
    @TableField(value = "is_sale")
    private Integer isSale;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}