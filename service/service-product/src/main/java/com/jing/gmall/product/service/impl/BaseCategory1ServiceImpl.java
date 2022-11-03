package com.jing.gmall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.item.vo.CategoryView;
import com.jing.gmall.product.entity.BaseCategory1;
import com.jing.gmall.product.service.BaseCategory1Service;
import com.jing.gmall.product.mapper.BaseCategory1Mapper;
import com.jing.gmall.weball.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Jing
* @description 针对表【base_category1(一级分类表)】的数据库操作Service实现
* @createDate 2022-10-30 18:14:38
*/
@Service
public class BaseCategory1ServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1>
    implements BaseCategory1Service{

    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    /**
     * 获取所有 分类列表的树型信息
     * @return
     */
    @Override
    public List<CategoryVo> getCategoryTreeData() {
        return baseCategory1Mapper.getCategoryTreeData();
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




