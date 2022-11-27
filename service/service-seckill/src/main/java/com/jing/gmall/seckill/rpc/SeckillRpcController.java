package com.jing.gmall.seckill.rpc;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.common.util.DateUtil;
import com.jing.gmall.seckill.entity.SeckillGoods;
import com.jing.gmall.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/inner/rpc/seckill")
public class SeckillRpcController {


    @Autowired
    private SeckillService seckillService;

    /**
     * 获取当天所有参与秒杀的商品列表
     * @return
     */
    @GetMapping("/today/goods")
    public Result<List<SeckillGoods>> getSeckillGoods(){
        String date = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
        List<SeckillGoods> goods = seckillService.getSeckillGoods(date);
        return Result.ok(goods);
    }


    /**
     * 获取秒杀商品的详情信息
     * @param skuId
     * @return
     */
    @GetMapping("/getDetail/{skuId}")
    public Result<SeckillGoods> getDetail(@PathVariable("skuId") Long skuId){
        SeckillGoods seckillGoods = seckillService.getSeckillDetail(skuId);
        return Result.ok(seckillGoods);
    }
}
