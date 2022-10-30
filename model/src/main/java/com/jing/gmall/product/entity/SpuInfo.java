package com.jing.gmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 商品表
 * @TableName spu_info
 */
@TableName(value ="spu_info")
@Data
public class SpuInfo implements Serializable {
    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    @TableField(value = "spu_name")
    private String spuName;

    /**
     * 商品描述(后台简述）
     */
    @TableField(value = "description")
    private String description;

    /**
     * 三级分类id
     */
    @TableField(value = "category3_id")
    private Long category3Id;

    /**
     * 品牌id
     */
    @TableField(value = "tm_id")
    private Long tmId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}