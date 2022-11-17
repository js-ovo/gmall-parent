package com.jing.gmall.common.constant;

/**
 * redis相关常量
 */
public class RedisConst {
    /** 商品详情bitmap的key*/
    public static final String SKUID_BITMAP_KEY = "skuids";

    /**分布式锁前缀*/
    public static final String LOCK_PREFIX = "lock:";

    /** 缓存商品详情key*/
    public static final String SKU_INFO_CACHE_KEY = "sku:info:";

    /** 商品全部分类的key*/
    public static final String CATEGORY_CACHE_KEY = "category";

    public static final String HOT_SCORE_KEY = "hotscore:";
    public static final String USER_LOGIN_KEY = "user:info:";
    public static final long USER_AUTH_TTL = 7;
    public static final String USER_UID = "uid";
    public static final String TEMP_ID = "usertempid";

    /**
     * Redis存储用户 购物车信息对应的key
     */
    public static final String CART_INFO_KEY = "cart:info:";
    /**
     * 单个商品最大数量
     */
    public static final Integer CART_ITEM_MAX_VALUE = 200;
    /**
     * 购物车最大 商品数目
     */
    public static final Long CART_ITEM_MAX_COUNT = 200L;
}
