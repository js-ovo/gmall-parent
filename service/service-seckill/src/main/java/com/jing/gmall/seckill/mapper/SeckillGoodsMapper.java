package com.jing.gmall.seckill.mapper;

import com.jing.gmall.seckill.entity.SeckillGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Jing
* @description 针对表【seckill_goods】的数据库操作Mapper
* @createDate 2022-11-25 20:34:37
* @Entity com.jing.gmall.seckill.entity.SeckillGoods
*/
public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

    /**
     * 获取指定日期的秒杀商品列表
     * @param date
     * @return
     */
    List<SeckillGoods> getSeckillGoods(@Param("date") String date);

    void decrementStock(@Param("id") Long id);
}




