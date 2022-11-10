package com.jing.gmall.cache.servie;


import com.fasterxml.jackson.core.type.TypeReference;

import java.util.concurrent.TimeUnit;

public interface CacheService {
    //SkuDetailVo getCacheData(String cacheKey);

   // boolean existSkuIdBitMap(Long skuId);

    void saveCache(String cacheKey, Object  dataVal);

    void saveCache(String cacheKey, Object retVal, long timeout, TimeUnit unit);

    <T> T getCacheDataObj(String cacheKey, Class<T> clazz);

    <T> T getCacheDataObj(String cacheKey, TypeReference<T> tTypeReference);

    boolean exitBitMap(String bitmapKey, Long index);

    void delayedDoubleDel(String cacheKey);

}
