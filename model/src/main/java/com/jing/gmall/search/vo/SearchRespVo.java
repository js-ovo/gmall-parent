package com.jing.gmall.search.vo;

import com.jing.gmall.search.entity.Goods;
import lombok.Data;

import java.util.List;

/**
 * 商品检索返回数据的vo
 */
@Data
public class SearchRespVo {

    /**
     * 商品的搜索信息
     */
    private SearchParamVo searchParam;

    /**
     * 品牌面包屑,点击品牌搜索 页面显示的小方框
     */
    private String trademarkParam;

    /**
     * 平台属性面包屑 点击平台属性 显示小方框  内存多大 电池容量等
     */
    private List<AttrVo> propsParamList;

    /**
     * 商品的品牌列表   搜索到的商品包含的品牌
     */
    private List<TrademarkVo> trademarkList;

    /**
     * 商品的属性名和属性值 搜索到的商品所包含的
     */
    private List<AttrListItemVo> attrsList;

    /**
     *  路径   http://sph-list.atguigu.cn/list.html?category3Id=61
     */
    private String urlParam;

    /**
     * 商品列表
     */
    private List<Goods> goodsList;

    /**
     * 分页排序信息
     */
    private OrderMap orderMap;
    /**
     * 当前页数
     */
    private Integer pageNo;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 平台属性面包屑
     */
    @Data
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }

    /**
     * 品牌列表
     */
    @Data
    public static class TrademarkVo {
        private Long tmId;
        private String tmName;
        private String tmLogoUrl;
    }

    /**
     * 平台属性
     */
    @Data
    public static class AttrListItemVo {
        private Long attrId;
        private String attrName;
        /**
         * 属性值集合
         */
        private List<String> attrValueList;
    }

    /**
     * 排序  默认按照热度降序
     */
    @Data
    public static class OrderMap {
        /**
         * 排序类型  综合 价格等   1-2取值
         */
        private String type = "1";
        /**
         * 排序方式 desc asc
         */
        private String sort = "desc";
    }
}
