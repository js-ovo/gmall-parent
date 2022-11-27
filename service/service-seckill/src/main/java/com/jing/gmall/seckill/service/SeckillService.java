package com.jing.gmall.seckill.service;

import com.jing.gmall.common.result.ResultCodeEnum;
import com.jing.gmall.seckill.entity.SeckillGoods;

import java.util.List;

public interface SeckillService {
    void upSeckillGoos(String date);
    List<SeckillGoods> getSeckillGoods(String data);
    SeckillGoods getSeckillDetail(Long skuId);


    /**
     * 生成商品秒杀码
     * @param skuId
     * @return
     */
    String generateSeckillCode(Long skuId);

    /**
     * 秒杀下单
     * @param skuId
     * @param code
     */
    void saveSeckillOrder(Long skuId, String code);

    /**
     * 检查秒杀单状态
     * @param userId
     * @param skuId
     * @return
     */
    ResultCodeEnum checkOrderStatus(Long userId, Long skuId);
}
