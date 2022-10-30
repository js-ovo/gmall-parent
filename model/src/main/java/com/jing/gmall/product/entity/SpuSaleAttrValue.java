package com.jing.gmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * spu销售属性值
 * @TableName spu_sale_attr_value
 */
@TableName(value ="spu_sale_attr_value")
@Data
public class SpuSaleAttrValue implements Serializable {
    /**
     * 销售属性值编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    @TableField(value = "spu_id")
    private Long spuId;

    /**
     * 销售属性id
     */
    @TableField(value = "base_sale_attr_id")
    private Long baseSaleAttrId;

    /**
     * 销售属性值名称
     */
    @TableField(value = "sale_attr_value_name")
    private String saleAttrValueName;

    /**
     * 销售属性名称(冗余)
     */
    @TableField(value = "sale_attr_name")
    private String saleAttrName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}