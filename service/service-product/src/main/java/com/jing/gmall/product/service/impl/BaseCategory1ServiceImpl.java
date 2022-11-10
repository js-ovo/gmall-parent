package com.jing.gmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.cache.annotation.MallCache;
import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.item.vo.CategoryView;
import com.jing.gmall.product.entity.BaseCategory1;
import com.jing.gmall.product.mapper.BaseCategory1Mapper;
import com.jing.gmall.product.service.BaseCategory1Service;
import com.jing.gmall.weball.vo.CategoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
* @author Jing
* @description 针对表【base_category1(一级分类表)】的数据库操作Service实现
* @createDate 2022-10-30 18:14:38
*/
@Service
@Slf4j
public class BaseCategory1ServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1>
    implements BaseCategory1Service{

    //引入本地缓存
    private Map<String,List<CategoryVo>> cache = new ConcurrentHashMap<>();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;




    // 使用本地缓存
    public List<CategoryVo> getCategoryTreeDataByLocal() {
        // 从本地缓存中查
        List<CategoryVo> categorys = cache.get("categorys");
        if ( categorys == null){
            // 没有去数据库查询
            List<CategoryVo> treeData = baseCategory1Mapper.getCategoryTreeData();
            // 数据回源
            cache.put("categorys",treeData);
            return treeData;
        }
        // 有的话直接返回
        return categorys;
    }


    /**
     * 获取所有 分类列表的树型信息
     * @return
     */
    @MallCache(cacheKey = RedisConst.CATEGORY_CACHE_KEY,
    timeout = 7,
    unit = TimeUnit.DAYS)
    @Override
    public List<CategoryVo> getCategoryTreeData() {
        return baseCategory1Mapper.getCategoryTreeData();
    }


    /**
     * 获取所有 分类列表的树型信息
     * @return
     */
    //分布式缓存redis
    public List<CategoryVo> getCategoryTreeDataWithCache() {
        //先去缓存中查找
        String categorys = stringRedisTemplate.opsForValue().get("categorys");
        if (!StringUtils.isEmpty(categorys)){
            // 缓存命中
            List<CategoryVo> list = JSON.parseArray(categorys, CategoryVo.class);
//            log.info("categorys-array: {}",list);
//            List<CategoryVo> list1 = JSON.parseObject(categorys, new TypeReference<List<CategoryVo>>() {});
//            log.info("categorys-object: {}" ,list1);
            return list;
        }
        // 缓存未命中 数据回溯
        List<CategoryVo> treeData = baseCategory1Mapper.getCategoryTreeData();
        // 无论是否查询到值 都进行回溯,防止 缓存穿透
        stringRedisTemplate.opsForValue().set("categorys", JSON.toJSONString(treeData));
        return treeData;
    }

    /**
     * 获取某个商品对应的全部分类信息
     * @param category3Id
     * @return
     */
    @Override
    public CategoryView getCategoryView(Long category3Id) {
        return baseCategory1Mapper.getCategoryView(category3Id);
    }
}




