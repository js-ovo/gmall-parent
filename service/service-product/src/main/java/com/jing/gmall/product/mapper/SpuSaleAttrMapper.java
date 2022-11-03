package com.jing.gmall.product.mapper;

import com.jing.gmall.product.entity.SpuSaleAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jing.gmall.product.vo.SkuAttrValueVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Jing
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Mapper
* @createDate 2022-10-30 18:14:38
* @Entity com.jing.gmall.product.entity.SpuSaleAttr
*/
public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {

    /**
     * 获取销售属性  以及对应的值
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrList(@Param("spuId") Long spuId);

    List<SpuSaleAttr> getSpuSaleAttrAndValueListWithOrder(@Param("spuId") Long spuId, @Param("skuId") Long skuId);

    List<SkuAttrValueVo> getSkuAttrValueJson(@Param("spuId") Long spuId);
}




