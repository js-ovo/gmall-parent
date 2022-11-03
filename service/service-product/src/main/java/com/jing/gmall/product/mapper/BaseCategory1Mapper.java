package com.jing.gmall.product.mapper;

import com.jing.gmall.item.vo.CategoryView;
import com.jing.gmall.product.entity.BaseCategory1;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jing.gmall.weball.vo.CategoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Jing
* @description 针对表【base_category1(一级分类表)】的数据库操作Mapper
* @createDate 2022-10-30 18:14:38
* @Entity com.jing.gmall.product.entity.BaseCategory1
*/
public interface BaseCategory1Mapper extends BaseMapper<BaseCategory1> {

    /**
     * 获取所有 分类列表的树型信息
     * @return
     */
    List<CategoryVo> getCategoryTreeData();

    /**
     * 获取对应商品的 全部分类信息
     * @param category3Id
     * @return
     */
    CategoryView getCategoryView(@Param("category3Id") Long category3Id);
}




