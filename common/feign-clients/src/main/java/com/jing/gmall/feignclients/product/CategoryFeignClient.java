package com.jing.gmall.feignclients.product;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.weball.vo.CategoryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("service-product")
@RequestMapping("/api/inner/rpc/product")
public interface CategoryFeignClient {
    /**
     * 获取分类列表信息
     * @return
     */
    @GetMapping("/category/tree")
    Result<List<CategoryVo>> getCategoryTreeData();
}
