package com.jing.gmall.item.vo;

import com.jing.gmall.product.entity.SkuInfo;
import com.jing.gmall.product.entity.SpuSaleAttr;
import lombok.Data;

import java.util.List;

/**
 * 返回给 web-all商品详细信息统一封装
 *
 */
@Data
public class SkuDetailVo {
    /**
     * 商品分类
     */
    private CategoryView categoryView;
    /**
     * 商品的基本信息 + 图片
     */
    private SkuInfo skuInfo;
    /**
     * 商品销售属性信息
     */
    private List<SpuSaleAttr> spuSaleAttrList;
    /**
     * 其他json数据
     */
    private String valuesSkuJson;
}
