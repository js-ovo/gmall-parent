package com.jing.gmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 商品图片表
 * @TableName spu_image
 */
@TableName(value ="spu_image")
@Data
public class SpuImage implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    @TableField(value = "spu_id")
    private Long spuId;

    /**
     * 图片名称
     */
    @TableField(value = "img_name")
    private String imgName;

    /**
     * 图片路径
     */
    @TableField(value = "img_url")
    private String imgUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}