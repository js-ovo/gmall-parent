package com.jing.gmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * sku平台属性值关联表
 * @TableName sku_attr_value
 */
@TableName(value ="sku_attr_value")
@Data
public class SkuAttrValue implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 属性id（冗余)
     */
    @TableField(value = "attr_id")
    private Long attrId;

    /**
     * 属性值id
     */
    @TableField(value = "value_id")
    private Long valueId;

    /**
     * skuid
     */
    @TableField(value = "sku_id")
    private Long skuId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}