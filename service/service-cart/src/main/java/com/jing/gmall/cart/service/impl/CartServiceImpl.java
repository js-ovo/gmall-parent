package com.jing.gmall.cart.service.impl;

import com.jing.gmall.cart.entity.CartInfo;
import com.jing.gmall.cart.service.CartService;
import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.common.execption.GmallException;
import com.jing.gmall.common.result.ResultCodeEnum;
import com.jing.gmall.common.util.Jsons;
import com.jing.gmall.common.utils.HttpRequestUtils;
import com.jing.gmall.feignclients.product.SkuFeignClient;
import com.jing.gmall.product.entity.SkuInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SkuFeignClient skuFeignClient;

    @Override
    public String resolveKey() {
        Long userId = HttpRequestUtils.getUserId();
        if (userId == null){
            // 使用临时用户
            return RedisConst.CART_INFO_KEY + HttpRequestUtils.getTempId();
        }
        return RedisConst.CART_INFO_KEY + userId;
    }

    @Override
    public SkuInfo addProductToCart(String key, Long skuId, Integer skuNum) {

        SkuInfo skuInfo = new SkuInfo();
        // 判断当前用户的购物车中是否有该商品 有  更新商品数量  没有 新加
        Boolean hasProduct = stringRedisTemplate.opsForHash().hasKey(key, skuId.toString());
        if (! hasProduct){
            // 新增  判断是否超过购物车最大容量
            if (stringRedisTemplate.opsForHash().size(key) >= RedisConst.CART_ITEM_MAX_COUNT){
                throw  new GmallException(ResultCodeEnum.CART_MAX_ITEM_COUNT);
            }
            // 获取商品信息  远程调用
            skuInfo = skuFeignClient.getSkuInfo(skuId).getData();
            CartInfo cartInfo = new CartInfo();
            cartInfo.setSkuId(skuId);
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setSkuPrice(skuInfo.getPrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());

            // 将商品加入购物车
            saveProduct(key, cartInfo);
            return skuInfo;
        }
        // 更新商品数量   获取 当前购物车中的 项目
        CartInfo cartInfo = getCartInfo(key, skuId);
        // 更新商品数量
        cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
        // 存入购物车
        saveProduct(key,cartInfo);
        // 返回skuInfo
        skuInfo.setId(skuId);
        skuInfo.setSkuName(cartInfo.getSkuName());
        skuInfo.setSkuDefaultImg(cartInfo.getImgUrl());

        //  给临时用户 购物车设置过期时间
        if (HttpRequestUtils.getUserId() == null && stringRedisTemplate.getExpire(key) < 0){
            stringRedisTemplate.expire(key, Duration.ofDays(365));
        }
        return skuInfo;
    }

    /**
     * 获取购物车列表
     * @param key
     * @return
     */
    @Override
    public List<CartInfo> getCartList(String key) {

        // 如果 是登录用户，合并购物车
        Long userId = HttpRequestUtils.getUserId();
        if (userId != null){
            String tempId = HttpRequestUtils.getTempId();
            if (! StringUtils.isEmpty(tempId)){
                List<Object> carts = stringRedisTemplate.opsForHash().values(RedisConst.CART_INFO_KEY + tempId);
                if (carts != null && carts.size() > 0){
                    log.info("登录用户,发现有临时用户,准备合并 购物车。。。。。");
                    carts.forEach(item -> {
                        CartInfo cartInfo = Jsons.json2Obj(item.toString(), CartInfo.class);
                        log.info("正在合并【{}】号商品",cartInfo.getSkuId());
                        // 每一个商品调用 添加商品方法
                        addProductToCart(key,cartInfo.getSkuId(),cartInfo.getSkuNum());
                    });
                    // 删除临时购物车 数据
                    stringRedisTemplate.delete(RedisConst.CART_INFO_KEY + tempId);
                }
            }

        }

        // 再查询   登录用户 先合并后再查询   临时用户 直接查询
        List<Object> values = stringRedisTemplate.opsForHash().values(key);

        return values.stream()
                .map(item -> Jsons.json2Obj(item.toString(), CartInfo.class))
                .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())) // 按照时间排序
                .collect(Collectors.toList());
    }


    /**
     * 更新购物车 商品数量
     * @param key
     * @param skuId
     * @param num
     */
    @Override
    public void updateProductCount(String key, Long skuId, Integer num) {
        // 获取商品信息
        CartInfo cartInfo = getCartInfo(key, skuId);
        // 更新数量
        cartInfo.setSkuNum(cartInfo.getSkuNum() + num);
        // 存入redis
        saveProduct(key,cartInfo);
    }


    /**
     * 修改商品选中状态
     * @param key
     * @param skuId
     * @param status
     */
    @Override
    public void updateCheckStatus(String key, Long skuId, Integer status) {
        CartInfo cartInfo = getCartInfo(key, skuId);
        cartInfo.setIsChecked(status);
        saveProduct(key,cartInfo);
    }

    /**
     * 从购物车中移除该商品
     * @param key
     * @param skuId
     */
    @Override
    public void removeProduct(String key, Long skuId) {
        stringRedisTemplate.opsForHash().delete(key,skuId.toString());
    }

    /**
     * 删除  选中的商品
     * @param key
     */
    @Override
    public void deleteChecked(String key) {
        // 获取所有的商品列表
        List<CartInfo> cartList = getCartList(key);
        if (cartList.size() <= 0){
            throw new GmallException(ResultCodeEnum.CART_IS_NULL);
        }
        Object[] skuIds = cartList.stream().filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .map(cartInfo -> cartInfo.getSkuId().toString()).toArray();
        if (skuIds.length <= 0){
            throw new GmallException(ResultCodeEnum.CART_NO_CHECKED);
        }
        stringRedisTemplate.opsForHash().delete(key,skuIds);
    }


    /**
     * 商品信息存入redis
     * @param key
     * @param cartInfo
     */
    private void saveProduct(String key,CartInfo cartInfo) {
        // 判断是否超过单个商品最大数量
        if (cartInfo.getSkuNum() > RedisConst.CART_ITEM_MAX_VALUE){
            throw new GmallException(ResultCodeEnum.CART_ITEM_COUNT_FLOWER);
        }
        stringRedisTemplate.opsForHash().put(key,cartInfo.getSkuId().toString(), Jsons.toJsonStr(cartInfo));
    }


    /**
     * 获取 购物车中对应的商品
     * @param key
     * @param skuId
     * @return
     */
    private CartInfo getCartInfo(String key, Long skuId) {
        Object product = stringRedisTemplate.opsForHash().get(key, skuId.toString());
        return Jsons.json2Obj(product.toString(), CartInfo.class);
    }
}
