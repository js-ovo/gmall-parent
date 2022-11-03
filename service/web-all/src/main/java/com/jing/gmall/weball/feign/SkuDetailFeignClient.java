package com.jing.gmall.weball.feign;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.item.vo.SkuDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-item")
@RequestMapping("/api/inner/rpc/item")
public interface SkuDetailFeignClient {
    @GetMapping("/product/{skuId}")
    Result<SkuDetailVo> getSkuDetail(@PathVariable("skuId") Long skuId);
}
