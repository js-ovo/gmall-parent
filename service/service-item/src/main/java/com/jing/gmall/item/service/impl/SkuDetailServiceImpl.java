package com.jing.gmall.item.service.impl;

import com.jing.gmall.cache.annotation.MallCache;
import com.jing.gmall.cache.servie.CacheService;
import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.item.feign.SkuFeignClient;
import com.jing.gmall.item.service.SkuDetailService;
import com.jing.gmall.item.vo.CategoryView;
import com.jing.gmall.item.vo.SkuDetailVo;
import com.jing.gmall.product.entity.SkuImage;
import com.jing.gmall.product.entity.SkuInfo;
import com.jing.gmall.product.entity.SpuSaleAttr;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ThreadPoolExecutor poolExecutor; //自定义线程池


    /**
     * 使用aop切面
     * @param skuId
     * @return
     */
    @MallCache(cacheKey = RedisConst.SKU_INFO_CACHE_KEY + "#{#skuId}"
    ,bitMapKey = RedisConst.SKUID_BITMAP_KEY
    ,bitIndex = "#{#skuId}"
    ,timeout = 30
    ,unit = TimeUnit.MINUTES)
    @Override
    public SkuDetailVo getSkuDetail(Long skuId) {
        return getSkuDetailVoFromRpc(skuId);
    }

    /**
     * 引入缓存查询 商品的详情信息  没有使用aop
     * @param skuId
     * @return
     */
    public SkuDetailVo getSkuDetailNoAop(Long skuId) {

        String cacheKey = "sku:info:" + skuId;
        //从缓存中查询数据
        SkuDetailVo cacheData = cacheService.getCacheDataObj(cacheKey,SkuDetailVo.class);
        //缓存命中 直接返回
        if (cacheData != null){
            return cacheData;
        }
        // 缓存未命中
        // 去bitMap中查找是否有该商品信息
        boolean isExist = cacheService.exitBitMap(RedisConst.SKUID_BITMAP_KEY,skuId);
        // 有 -> 远程调用查询
        if (isExist){
            log.info("位图判断,含有[{}]号商品,去数据库查询",skuId);
            // 缓存击穿风险
            // 获取锁  锁的粒度尽可能小,一个商品一个锁
            String lockKey = RedisConst.LOCK_PREFIX + cacheKey;
            RLock lock = redissonClient.getLock(lockKey);
            // 尝试加锁
            if (lock.tryLock()){
                // 获取到锁
                try {
                    // 远程调用 获取数据
                    //SkuDetailVo skuDetailVo = getSkuDetailVoFromRpc(skuId);
                    // 数据存入缓存
                    //cacheService.saveCache(cacheKey,skuDetailVo);
                    // 返回数据
                    //return skuDetailVo;
                } finally {
                    lock.unlock();
                }
            } else {
                // 没获取到锁 暂停几秒再查缓存
                log.info("没有获取到锁,稍等一会儿查询缓存");
                try {
                    // 睡1会儿
                    TimeUnit.MILLISECONDS.sleep(300);
                    // 再次查询缓存返回
                    //return getSkuDetailVoFromRpc(skuId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


        }
        // 没有 -> 返回null
       return null;
    }


    /**
     * 从远程服务查询数据库
     * 获取商品的详细信息
     * @param skuId
     * @return
     */

    //服务调用关系
    /** -获取商品详情信息
     *      获取商品分类信息
     *      获取商品图片信息
     *      获取商品的销售属性
     *      获取组合信息
     *  - 获取商品实时价格
     *
     *      异步调用优化
     */
    public SkuDetailVo getSkuDetailVoFromRpc(Long skuId) {

        SkuDetailVo skuDetailVo = new SkuDetailVo();
        // 获取商品的基本信息
        CompletableFuture<SkuInfo> skuInfoAsync = CompletableFuture.supplyAsync(() -> {
            log.info("skuInfoAsync:{}", Thread.currentThread().getName());
            return skuFeignClient.getSkuInfo(skuId).getData();
        }, poolExecutor);

        // 获取商品的所有图片信息
        CompletableFuture<Void> imageAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {
            List<SkuImage> images = skuFeignClient.getSkuImages(skuId).getData();
            skuInfo.setSkuImageList(images);
            log.info("imageAsync:{}",Thread.currentThread().getName());
            skuDetailVo.setSkuInfo(skuInfo);
        }, poolExecutor);

        // 获取商品分类完整路径
        CompletableFuture<Void> categoryViewAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {
            CategoryView categoryView = skuFeignClient.getCategoryView(skuInfo.getCategory3Id()).getData();
            skuDetailVo.setCategoryView(categoryView);
            log.info("{}",Thread.currentThread().getName());
        },poolExecutor);

        // 获取商品的所有销售属性
        CompletableFuture<Void> spuSaleAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {
            List<SpuSaleAttr> spuSaleAttrs = skuFeignClient.getSpuSaleAttrAndValue(skuInfo.getSpuId(), skuId).getData();
            skuDetailVo.setSpuSaleAttrList(spuSaleAttrs);
            log.info("spuSaleAsync:{}",Thread.currentThread().getName());
        }, poolExecutor);

        // 获取skuJson
        CompletableFuture<Void> jsonAsync = skuInfoAsync.thenAcceptAsync(skuInfo -> {
            String json = skuFeignClient.getSkuJson(skuInfo.getSpuId()).getData();
            skuDetailVo.setValuesSkuJson(json);
            log.info("jsonAsync:{}",Thread.currentThread().getName());
        }, poolExecutor);


        // 获取商品的实时价格
        CompletableFuture<Void> priceAsync = CompletableFuture.runAsync(() -> {
            BigDecimal price = skuFeignClient.getRealtimePrice(skuId).getData();
            skuDetailVo.setPrice(price);
            log.info("priceAsync:{}",Thread.currentThread().getName());
        }, poolExecutor);

        CompletableFuture.allOf(categoryViewAsync,imageAsync,spuSaleAsync,jsonAsync,priceAsync)
                .join();

        return skuDetailVo;
    }
}
