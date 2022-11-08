package com.jing.gmall.item.cache.servie;

import com.jing.gmall.item.vo.SkuDetailVo;

public interface CacheService {
    SkuDetailVo getCacheData(String cacheKey);

    boolean existSkuIdBitMap(Long skuId);

    void saveCache(String cacheKey, SkuDetailVo skuDetailVo);
}
