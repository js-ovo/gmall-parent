package com.jing.gmall.cart.service;

import com.jing.gmall.cart.entity.CartInfo;
import com.jing.gmall.product.entity.SkuInfo;

import java.util.List;

public interface CartService {
    /**
     *  决定存储到购物车使用哪个key  tempId还是uid
     * @return
     */
    String resolveKey();

    /**
     *  将商品加入购物车
     * @param key
     * @param skuId
     * @param skuNum
     * @return
     */
    SkuInfo addProductToCart(String key, Long skuId, Integer skuNum);


    /**
     * 获取购物车列表
     * @param key
     * @return
     */
    List<CartInfo> getCartList(String key);

    void updateProductCount(String key, Long skuId, Integer num);

    void updateCheckStatus(String key, Long skuId, Integer status);

    void removeProduct(String key, Long skuId);

    void deleteChecked(String key);
}
