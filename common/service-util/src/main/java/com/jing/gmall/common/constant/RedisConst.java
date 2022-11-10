package com.jing.gmall.common.constant;

/**
 * redis相关常量
 */
public class RedisConst {
    // 商品详情bitmap的key
    public static final String SKUID_BITMAP_KEY = "skuids";

    // 分布式锁前缀
    public static final String LOCK_PREFIX = "lock";

    // 缓存商品详情key的前置
    public static final String SKU_INFO_CACHE_KEY = "sku:info:";

    // 商品全部分类的key
    public static final String CATEGORY_CACHE_KEY = "category";

}
