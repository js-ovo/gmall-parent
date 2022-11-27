package com.jing.gmall.weball.controller;

import com.jing.gmall.feignclients.seckill.SeckillFeignClient;
import com.jing.gmall.seckill.entity.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SeckillController {

    @Autowired
    private SeckillFeignClient seckillFeignClient;

    /**
     * 跳转到 秒杀页面 并返回秒杀的商品列表
     * @param model
     * @return
     */
    @GetMapping("/seckill.html")
    public String seckillPage(Model model){
        List<SeckillGoods> goods = seckillFeignClient.getSeckillGoods().getData();
        model.addAttribute("list",goods);
        return "seckill/index";
    }


    /**
     * 秒杀商品 详情页
     * @param skuId
     * @return
     */
    @GetMapping("/seckill/{skuId}.html")
    public String seckillDetail(@PathVariable("skuId") Long skuId,Model model){
        SeckillGoods goods = seckillFeignClient.getDetail(skuId).getData();
        model.addAttribute("item",goods);
        return "seckill/item";
    }


    /**
     * 秒杀商品排队页面
     * @param skuId
     * @param code
     * @param model
     * @return
     */
    @GetMapping("/seckill/queue.html")
    public String seckillQueue(@RequestParam("skuId") Long skuId,
                               @RequestParam("skuIdStr") String code,Model model){

        model.addAttribute("skuId",skuId);
        model.addAttribute("skuIdStr",code);
        return "seckill/queue";
    }
}
