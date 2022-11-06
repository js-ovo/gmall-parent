package com.jing.gmall.item.service;

import com.jing.gmall.item.vo.SkuDetailVo;

public interface CacheService {
    SkuDetailVo getCacheData(String cacheKey);

    boolean existSkuIdBitMap(Long skuId);

    void saveCache(String cacheKey, SkuDetailVo skuDetailVo);
}
