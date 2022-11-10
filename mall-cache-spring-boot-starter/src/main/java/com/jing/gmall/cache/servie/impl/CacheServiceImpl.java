package com.jing.gmall.cache.servie.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jing.gmall.cache.servie.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    ObjectMapper objectMapper = new ObjectMapper();


//    /**
//     * 从缓存中查询指定数据
//     * @param cacheKey
//     * @return
//     */
//    public SkuDetailVo getCacheData(String cacheKey) {
//        String data = stringRedisTemplate.opsForValue().get(cacheKey);
//        if (!StringUtils.isEmpty(data)){
//            try {
//                return objectMapper.readValue(data,SkuDetailVo.class);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    /**
     * 在bitmap中判断是否存在该数据
     * @param
     * @return
     */
//    @Override
//    public boolean existSkuIdBitMap(Long skuId) {
//        return stringRedisTemplate.opsForValue().getBit(RedisConst.SKUID_BITMAP_KEY, skuId);
//    }

    @Override
    public void saveCache(String cacheKey, Object dataVal) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(dataVal);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 将数据存入缓存
        stringRedisTemplate.opsForValue().set(cacheKey,json);
    }


    /**
     * 将数据存入 缓存并指定过期时间
     * @param cacheKey key
     * @param retVal data
     * @param timeout  过期时间
     * @param unit 单位
     */
    @Override
    public void saveCache(String cacheKey, Object retVal, long timeout, TimeUnit unit) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(retVal);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        stringRedisTemplate.opsForValue().set(cacheKey,json,timeout,unit);
    }




    /**
     *  查询bitmap中是否存在该信息
     * @param bitmapKey
     * @param skuId
     * @return
     */
    @Override
    public boolean exitBitMap(String bitmapKey, Long skuId) {
        return stringRedisTemplate.opsForValue().getBit(bitmapKey,skuId);
    }




    /**
     * 将缓存中的数据转换成简单类型
     * @param cacheKey key
     * @param clazz 返回的类型
     * @param <T>
     * @return
     */
    @Override
    public <T> T getCacheDataObj(String cacheKey, Class<T> clazz) {
        String json = stringRedisTemplate.opsForValue().get(cacheKey);
        if (! StringUtils.isEmpty(json)){
            try {
                return objectMapper.readValue(json,clazz);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 获取 缓存中的数据转换为指定类型,包含复杂泛型
     * @param cacheKey
     * @param tTypeReference
     * @param <T>
     * @return
     */

    @Override
    public <T> T getCacheDataObj(String cacheKey, TypeReference<T> tTypeReference) {
        String json = stringRedisTemplate.opsForValue().get(cacheKey);
        if (! StringUtils.isEmpty(json)){
            T t = null;
            try {
                t = objectMapper.readValue(json,tTypeReference);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return t;
        }
        return null;
    }
}
