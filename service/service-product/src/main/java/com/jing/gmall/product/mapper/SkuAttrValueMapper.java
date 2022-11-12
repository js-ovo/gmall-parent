package com.jing.gmall.product.mapper;

import com.jing.gmall.product.entity.SkuAttrValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jing.gmall.search.entity.SearchAttr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Jing
* @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Mapper
* @createDate 2022-10-30 18:14:38
* @Entity com.jing.gmall.product.entity.SkuAttrValue
*/
public interface SkuAttrValueMapper extends BaseMapper<SkuAttrValue> {

    List<SearchAttr> getSkuAttrNameAndValue(@Param("skuId") Long skuId);
}




