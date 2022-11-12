package com.jing.gmall.product.service;

import com.jing.gmall.product.entity.SkuAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.gmall.search.entity.SearchAttr;

import java.util.List;

/**
* @author Jing
* @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Service
* @createDate 2022-10-30 18:14:38
*/
public interface SkuAttrValueService extends IService<SkuAttrValue> {

    List<SearchAttr> getSkuAttrNameAndValue(Long skuId);
}
