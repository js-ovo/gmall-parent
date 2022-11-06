package com.jing.gmall.product;

import com.jing.gmall.product.service.SkuSaleAttrValueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ShardingSphereTest {
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    /**
     * ShardingSphere 读写分离测试
     */
    @Test
    public void balanceTest(){
        System.out.println(skuSaleAttrValueService.getById(115));
        System.out.println(skuSaleAttrValueService.getById(115));
        System.out.println(skuSaleAttrValueService.getById(115));
        System.out.println(skuSaleAttrValueService.getById(115));
    }
}
