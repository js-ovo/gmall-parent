package com.jing.gmall.product.service;

import com.jing.gmall.product.entity.SkuInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.gmall.product.vo.SkuInfoVo;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Jing
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2022-10-30 18:14:38
*/
public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfoVo skuInfoVo);

    BigDecimal getRealtimePrice(Long skuId);

    List<Long> getSkuIds();

    void removeSku(Long skuId);
}
