package com.jing.gmall.feignclients.ware;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ware-manage",url = "http://localhost:9001")
public interface WareFeignClient {

    /**
     * 查看 商品是否有库存
     * @param skuId 商品id
     * @param num 下单数量
     * @return
     */
    @GetMapping("/hasStock")
    String hasStock(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num);
}
