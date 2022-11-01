package com.jing.gmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 库存单元图片表
 * @TableName sku_image
 */
@TableName(value ="sku_image")
@Data
public class SkuImage implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    @TableField(value = "sku_id")
    private Long skuId;

    /**
     * 图片名称（冗余）
     */
    @TableField(value = "img_name")
    private String imgName;

    /**
     * 图片路径(冗余)
     */
    @TableField(value = "img_url")
    private String imgUrl;

    /**
     * 商品图片id
     */
    @TableField(value = "spu_img_id")
    private Long spuImgId;

    /**
     * 是否默认
     */
    @TableField(value = "is_default")
    private Integer isDefault;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}