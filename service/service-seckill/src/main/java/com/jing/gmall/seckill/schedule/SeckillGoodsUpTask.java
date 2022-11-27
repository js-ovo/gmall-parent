package com.jing.gmall.seckill.schedule;

import com.jing.gmall.common.util.DateUtil;
import com.jing.gmall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时上架要秒杀的商品
 */
@Component
@Slf4j
public class SeckillGoodsUpTask {

    @Autowired
    private SeckillService seckillService;

    // 秒 分 时 日 月 周
    //@Scheduled(cron = "0 0 3 * * ?")
    @Scheduled(cron = "0 * * * * ?")
    public void upGoods(){
        String date = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
        seckillService.upSeckillGoos(date);
    }
}
