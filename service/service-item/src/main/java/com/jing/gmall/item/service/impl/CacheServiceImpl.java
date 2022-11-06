package com.jing.gmall.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.item.service.CacheService;
import com.jing.gmall.item.vo.SkuDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 从缓存中查询指定数据
     * @param cacheKey
     * @return
     */
    @Override
    public SkuDetailVo getCacheData(String cacheKey) {
        String data = stringRedisTemplate.opsForValue().get(cacheKey);
        if (!StringUtils.isEmpty(data)){
            return JSON.parseObject(data,SkuDetailVo.class);
        }
        return null;
    }

    /**
     * 在bitmap中判断是否存在该数据
     * @param skuId
     * @return
     */
    @Override
    public boolean existSkuIdBitMap(Long skuId) {
        return stringRedisTemplate.opsForValue().getBit(RedisConst.SKUID_BITMAP_KEY, skuId);
    }

    @Override
    public void saveCache(String cacheKey, SkuDetailVo skuDetailVo) {
        // 将数据存入缓存
        stringRedisTemplate.opsForValue().set(cacheKey,JSON.toJSONString(skuDetailVo));
    }
}
