package com.jing.gmall.search.vo;

import lombok.Data;

/**
 * 商品首页搜索使用参数
 */
@Data
public class SearchParamVo {

    /**
     * 搜索栏搜索的关键字
     */
    private String keyword;
    /**
     * 根据商品三级分类搜索
     */
    private Long category3Id;
    /**
     * 根据商品二级分类搜索
     */
    private Long category2Id;
    /**
     * 根据商品一级分类搜索
     */
    private Long category1Id;
    /**
     * 根据商品品牌搜素
     */
    private String trademark;
    /**
     * 商品的排序信息 默认按照 热度降序
     */
    private String order = "1:desc";
    /**
     * 商品的平台属性信息  电池容量    运行内存
     */
    private String[] props;
    /**
     * 页数
     */
    private Integer pageNo;
}
