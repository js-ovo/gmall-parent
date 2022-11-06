package com.jing.gmall.product.init;

import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.product.service.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 服务启动读取 数据库中的数据的 id 放入 bitmap中
 * 使用spring-boot监听器机制
 * 组件初始化机制
 */
@Configuration
@Slf4j
public class InitTaskService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SkuInfoService skuInfoService;

    @PostConstruct // 组件初始化好之后执行的操作
    public void initSkuIdBitMap(){
        log.info("init task 对象创建好之后,执行初始化 数据放入bitmap中");
        // 获取所有的商品信息的id
        List<Long> ids = skuInfoService.getSkuIds();
        // 在bitmap中存入
        ids.forEach(id -> {
            stringRedisTemplate.opsForValue().setBit(RedisConst.SKUID_BITMAP_KEY,id,true);
        });
        log.info("skuids初始化完成!");
    }
}
