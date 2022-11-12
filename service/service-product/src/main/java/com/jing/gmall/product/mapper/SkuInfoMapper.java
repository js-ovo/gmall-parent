package com.jing.gmall.product.mapper;

import com.jing.gmall.product.entity.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Jing
* @description 针对表【sku_info(库存单元表)】的数据库操作Mapper
* @createDate 2022-10-30 18:14:38
* @Entity com.jing.gmall.product.entity.SkuInfo
*/
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    BigDecimal getRealtimePrice(@Param("skuId") Long skuId);

    List<Long> getSkuIds();

    void updateSkuSaleStatus(@Param("skuId") Long skuId, @Param("status") Integer status);
}




