package com.jing.gmall.product.service;

import com.jing.gmall.item.vo.CategoryView;
import com.jing.gmall.product.entity.BaseCategory1;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.gmall.weball.vo.CategoryVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Jing
* @description 针对表【base_category1(一级分类表)】的数据库操作Service
* @createDate 2022-10-30 18:14:38
*/
@Service
public interface BaseCategory1Service extends IService<BaseCategory1> {

    List<CategoryVo> getCategoryTreeData();

    CategoryView getCategoryView(Long skuId);
}
