package com.jing.gmall.product.rpc;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.product.service.BaseCategory1Service;
import com.jing.gmall.weball.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 供远程调用获取商品分类
 */
@RestController
@RequestMapping("/api/inner/rpc/product")
public class CateGoryRpcController {

    @Autowired
    private BaseCategory1Service baseCategory1Service;

    /**
     * 获取分类列表信息
     * @return
     */
    @GetMapping("/category/tree")
    public Result<List<CategoryVo>> getCategoryTreeData(){
        List<CategoryVo> vos =  baseCategory1Service.getCategoryTreeData();
        return Result.ok(vos);
    }
}
