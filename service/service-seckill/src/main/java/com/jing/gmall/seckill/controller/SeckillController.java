package com.jing.gmall.seckill.controller;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.common.result.ResultCodeEnum;
import com.jing.gmall.common.utils.HttpRequestUtils;
import com.jing.gmall.seckill.service.SeckillService;
import com.mysql.jdbc.log.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity/seckill/auth")
@Slf4j
public class SeckillController {


    @Autowired
    private SeckillService seckillService;


    /**
     * 生成秒杀码
     * @param skuId
     * @return
     */
    @GetMapping("/getSeckillSkuIdStr/{skuId}")
    public Result getSeckillCode(@PathVariable("skuId") Long skuId){
        String code  = seckillService.generateSeckillCode(skuId);
        return Result.ok(code);
    }

    //http://api.gmall.com/api/activity/seckill/auth/seckillOrder/52?skuIdStr=89245fcd2e62d3d6779a39c690e55d55

    /**
     * 提交秒杀订单
     * @param skuId
     * @param code
     * @return
     */
    @PostMapping("/seckillOrder/{skuId}")
    public Result saveSeckillOrder(@PathVariable("skuId") Long skuId,
                                   @RequestParam("skuIdStr") String code){
        log.info("秒杀下单,skuId:{}, 开始。。。。。",skuId);
        seckillService.saveSeckillOrder(skuId,code);
        return Result.ok();
    }


    /**
     * 检查秒杀单状态
     * @return
     */
    @GetMapping("/checkOrder/{skuId}")
    public Result checkOrder(@PathVariable("skuId") Long skuId){
        Long userId = HttpRequestUtils.getUserId();
        ResultCodeEnum resultCodeEnum = seckillService.checkOrderStatus(userId,skuId);
        return Result.build("",resultCodeEnum);
    }

}
