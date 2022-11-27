package com.jing.gmall.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.seckill.entity.SeckillGoods;
import com.jing.gmall.seckill.service.SeckillGoodsService;
import com.jing.gmall.seckill.mapper.SeckillGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Jing
* @description 针对表【seckill_goods】的数据库操作Service实现
* @createDate 2022-11-25 20:34:37
*/
@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods>
    implements SeckillGoodsService{

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    
    
    @Override
    public List<SeckillGoods> getSeckillGoods(String date) {
        return seckillGoodsMapper.getSeckillGoods(date);
    }

    @Override
    public boolean decrementStock(Long id) {
        try {
            baseMapper.decrementStock(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}




