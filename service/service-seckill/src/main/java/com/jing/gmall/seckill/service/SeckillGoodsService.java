package com.jing.gmall.seckill.service;

import com.jing.gmall.seckill.entity.SeckillGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Jing
* @description 针对表【seckill_goods】的数据库操作Service
* @createDate 2022-11-25 20:34:37
*/
public interface SeckillGoodsService extends IService<SeckillGoods> {

    /**
     * 获取指定日期参与 秒杀的商品列表
     * @param date 指定日期字符串 格式为 yyyy-MM-dd
     * @return
     */
    List<SeckillGoods> getSeckillGoods(String date);

    /**
     * 根据id扣减秒杀商品的库存
     * @param id
     * @return
     */
    boolean decrementStock(Long id);
}
