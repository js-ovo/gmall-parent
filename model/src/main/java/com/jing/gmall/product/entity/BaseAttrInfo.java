package com.jing.gmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 属性表
 * @TableName base_attr_info
 */
@TableName(value ="base_attr_info")
@Data
public class BaseAttrInfo implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 属性名称
     */
    @TableField(value = "attr_name")
    private String attrName;

    /**
     * 分类id
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 分类层级
     */
    @TableField(value = "category_level")
    private Integer categoryLevel;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 封装平台属性的值
     */
    @TableField(exist = false)
    private List<BaseAttrValue> attrValueList;
}