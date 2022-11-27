package com.jing.gmall.feignclients.seckill;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.seckill.entity.SeckillGoods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("service-seckill")
@RequestMapping("/api/inner/rpc/seckill")
public interface SeckillFeignClient {


    /**
     * 获取当天参与秒杀的商品列表
     * @return
     */
    @GetMapping("/today/goods")
    Result<List<SeckillGoods>> getSeckillGoods();

    /**
     * 获取秒杀商品的详情信息
     * @param skuId
     * @return
     */
    @GetMapping("/getDetail/{skuId}")
    Result<SeckillGoods> getDetail(@PathVariable("skuId") Long skuId);
}
