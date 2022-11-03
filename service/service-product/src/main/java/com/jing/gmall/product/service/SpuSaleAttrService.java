package com.jing.gmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.gmall.product.entity.SpuSaleAttr;

import java.util.List;

/**
* @author Jing
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service
* @createDate 2022-10-30 18:14:38
*/
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    List<SpuSaleAttr> getSpuSaleAttrList(Long spuId);

    List<SpuSaleAttr> getSpuSaleAttrAndValueListWithOrder(Long spuId,Long skuId);

    String getSkuAttrValueJson(Long spuId);
}
