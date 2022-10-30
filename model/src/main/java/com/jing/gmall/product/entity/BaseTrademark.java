package com.jing.gmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 品牌表
 * @TableName base_trademark
 */
@TableName(value ="base_trademark")
@Data
public class BaseTrademark implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 属性值
     */
    @TableField(value = "tm_name")
    private String tmName;

    /**
     * 品牌logo的图片路径
     */
    @TableField(value = "logo_url")
    private String logoUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}