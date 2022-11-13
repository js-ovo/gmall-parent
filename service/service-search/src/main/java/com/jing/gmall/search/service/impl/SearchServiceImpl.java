package com.jing.gmall.search.service.impl;

import com.jing.gmall.common.constant.SearchConst;
import com.jing.gmall.search.entity.Goods;
import com.jing.gmall.search.repository.GoodsRepository;
import com.jing.gmall.search.service.SearchService;
import com.jing.gmall.search.vo.SearchParamVo;
import com.jing.gmall.search.vo.SearchRespVo;
import com.jing.gmall.search.vo.SearchRespVo.OrderMap;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public SearchRespVo search(SearchParamVo searchParamVo) {

        //TODO 搜索
        //构建检索条件
        Query query = buildSearchDSL(searchParamVo);
        //返回检索数据
        SearchHits<Goods> searchHits =
                elasticsearchRestTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));
        //分析处理数据
        SearchRespVo searchRespVo = buildSearchResp(searchHits,searchParamVo);
        return searchRespVo;
    }


    /**
     * 分析处理数据返回
     * @param searchHits
     * @param searchParamVo
     * @return
     */
    private SearchRespVo buildSearchResp(SearchHits<Goods> searchHits, SearchParamVo searchParamVo) {
        // 检索参数 原封不动返回
        SearchRespVo respVo = new SearchRespVo();
        respVo.setSearchParam(searchParamVo);
        // 品牌面包屑
        if (! StringUtils.isEmpty(searchParamVo.getTrademark())) {
            String trademark = searchParamVo.getTrademark().split(":")[1];
            respVo.setTrademarkParam("品牌:" + trademark);
        }

        // 属性面包屑
        if (searchParamVo.getProps() != null && searchParamVo.getProps().length > 0){
            List<SearchRespVo.AttrVo> attrVos = Arrays.stream(searchParamVo.getProps()).map(prop -> {
                String[] split = prop.split(":");
                String attrName = split[2];
                String attrValue = split[1];
                Long attrId = Long.parseLong(split[0]);
                SearchRespVo.AttrVo attrVo = new SearchRespVo.AttrVo();
                attrVo.setAttrId(attrId);
                attrVo.setAttrName(attrName);
                attrVo.setAttrValue(attrValue);
                return attrVo;
            }).collect(Collectors.toList());
            respVo.setPropsParamList(attrVos);
        }

        // 包含的品牌列表
        // 聚合查询
        Aggregations aggregations = searchHits.getAggregations();
        ParsedLongTerms tmIdAgg = aggregations.get("tmIdAgg");
        List<SearchRespVo.TrademarkVo> trademarkVos = tmIdAgg.getBuckets().stream().map(bucket -> {
            // 品牌id
            long tmId = bucket.getKeyAsNumber().longValue();
            ParsedStringTerms tmNameAgg = bucket.getAggregations().get("tmNameAgg");
            // 品牌名称
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();
            // 品牌url
            ParsedStringTerms tmLogoUrlAgg = bucket.getAggregations().get("tmLogoUrlAgg");
            String tmLogoUrl = tmLogoUrlAgg.getBuckets().get(0).getKeyAsString();
            SearchRespVo.TrademarkVo trademarkVo = new SearchRespVo.TrademarkVo();
            trademarkVo.setTmId(tmId);
            trademarkVo.setTmName(tmName);
            trademarkVo.setTmLogoUrl(tmLogoUrl);
            return trademarkVo;
        }).collect(Collectors.toList());
        respVo.setTrademarkList(trademarkVos);
        // 包含的所有属性以及值列表
        // 聚合查询

        ParsedNested attrNestedAgg = aggregations.get("attrAgg");
        Aggregations attrAgg = attrNestedAgg.getAggregations();
        ParsedLongTerms attrIdAgg = attrAgg.get("attrIdAgg");
        List<SearchRespVo.AttrListItemVo> attrListItemVos = attrIdAgg.getBuckets().stream().map(bucket -> {
            // 属性id
            long attrId = bucket.getKeyAsNumber().longValue();
            Aggregations agg = bucket.getAggregations();
            ParsedStringTerms attrNameAgg = agg.get("attrNameAgg");
            // 属性名
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            // 属性值 集合
            ParsedStringTerms attrValueAgg = agg.get("attrValueAgg");
            List<String> attrValueList = attrValueAgg.getBuckets().stream().map(
                    MultiBucketsAggregation.Bucket::getKeyAsString
            ).collect(Collectors.toList());
            SearchRespVo.AttrListItemVo attrListVo = new SearchRespVo.AttrListItemVo();
            attrListVo.setAttrId(attrId);
            attrListVo.setAttrName(attrName);
            attrListVo.setAttrValueList(attrValueList);
            return attrListVo;
        }).collect(Collectors.toList());

        respVo.setAttrsList(attrListItemVos);
        // url参数 返回给前端  从传入的参数进行拼接 list.html?xxx&xxxx
        String urlParam = convertRequestParamToUrlParam(searchParamVo);
        respVo.setUrlParam(urlParam);
        // 返回检索到的所有商品信息
        List<Goods> goodsList = searchHits.getSearchHits().stream().map(searchHit -> {
            Goods goods = searchHit.getContent();
            if (!StringUtils.isEmpty(searchParamVo.getKeyword())) {
                // 高亮显示 关键字
                String title = searchHit.getHighlightField("title").get(0);
                goods.setTitle(title);
            }
            return goods;
        }).collect(Collectors.toList());
        respVo.setGoodsList(goodsList);
        // 排序信息
        OrderMap orderMap = new OrderMap();
        if (!StringUtils.isEmpty(searchParamVo.getOrder())){
            String[] split = searchParamVo.getOrder().split(":");
            orderMap.setSort(split[1]);
            orderMap.setType(split[0]);
        }
        respVo.setOrderMap(orderMap);
        // 分页数据
        Integer pageNo = searchParamVo.getPageNo();
        respVo.setPageNo(pageNo == null ? SearchConst.DEFAULT_PAGE_NUM : pageNo);
        // 总页数
        // 总记录数
        long totalHits = searchHits.getTotalHits();
        Long totalPages = totalHits % SearchConst.DEFAULT_PAGE_SIZE == 0 ? totalHits / SearchConst.DEFAULT_PAGE_SIZE : totalHits / SearchConst.DEFAULT_PAGE_SIZE + 1;
        respVo.setTotalPages(totalPages.intValue());
        return respVo;
    }


    /**
     *  对url地址进行处理返回给前端
     * @param searchParamVo
     * @return
     */
    private String convertRequestParamToUrlParam(SearchParamVo searchParamVo) {
        StringBuilder stringBuilder = new StringBuilder("list.html?");

        if (searchParamVo.getCategory1Id() != null) {
            stringBuilder.append("&category1Id="+searchParamVo.getCategory1Id());
        }
        if (searchParamVo.getCategory2Id() != null) {
            stringBuilder.append("&category2Id="+searchParamVo.getCategory2Id());
        }
        if (searchParamVo.getCategory3Id() != null) {
            stringBuilder.append("&category3Id="+searchParamVo.getCategory3Id());
        }

        if (! StringUtils.isEmpty(searchParamVo.getKeyword())) {
            stringBuilder.append("&keyword="+searchParamVo.getKeyword());
        }

        if (searchParamVo.getProps() != null && searchParamVo.getProps().length > 0) {
            Arrays.stream(searchParamVo.getProps()).forEach(prop -> {
                stringBuilder.append("&props=" + prop);
            });
        }

        if (! StringUtils.isEmpty(searchParamVo.getTrademark())){
            String trademark = searchParamVo.getTrademark();
            stringBuilder.append("&trademark=" + trademark);
        }

        return stringBuilder.toString();
    }

    /**
     * 构建 原生查询dsl语句
     * @param searchParamVo
     * @return
     */
    private Query buildSearchDSL(SearchParamVo searchParamVo) {
        // 检索条件
        // 构建查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 根据分类查询
        if (searchParamVo.getCategory1Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category1Id",searchParamVo.getCategory1Id()));
        }
        if (searchParamVo.getCategory2Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category2Id",searchParamVo.getCategory2Id()));
        }
        if (searchParamVo.getCategory3Id() != null) {
            boolQuery.must(QueryBuilders.termQuery("category3Id",searchParamVo.getCategory3Id()));
        }

        //根据品牌检索
        if (!StringUtils.isEmpty(searchParamVo.getTrademark())) {
            long tmId = Long.parseLong(searchParamVo.getTrademark().split(":")[0]);
            boolQuery.must(QueryBuilders.termQuery("tmId",tmId));
        }

        // 根据关键字模糊检索
        if (! StringUtils.isEmpty(searchParamVo.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("title",searchParamVo.getKeyword()));
        }

        // 根据属性检索  props=23:8G:运行内存&props=24:128G:机身内存
        if (searchParamVo.getProps() != null && searchParamVo.getProps().length > 0) {
            Arrays.stream(searchParamVo.getProps()).forEach(item -> {
                String[] split = item.split(":");
                long attrId = Long.parseLong(split[0]);
                String attrValue = split[1];
                // 查询条件
                BoolQueryBuilder builder = QueryBuilders.boolQuery();
                builder.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                builder.must(QueryBuilders.termQuery("attrs.attrValue",attrValue));
                // 嵌套属性
                boolQuery.must(QueryBuilders.nestedQuery("attrs",builder, ScoreMode.None));
            });
        }
        // 原生查询条件
        NativeSearchQuery dcl = new NativeSearchQuery(boolQuery);
        //排序 分页
        Integer pageNo = SearchConst.DEFAULT_PAGE_NUM;
        if (searchParamVo.getPageNo() != null && searchParamVo.getPageNo() > 0) {
            pageNo = searchParamVo.getPageNo();
        }
        Pageable pageable = PageRequest.of(pageNo - 1,SearchConst.DEFAULT_PAGE_SIZE);
        dcl.setPageable(pageable);

        if (! StringUtils.isEmpty(searchParamVo.getOrder())) {
            String[] split = searchParamVo.getOrder().split(":");
            String type = split[0]; // 排序的属性类型
            String sort = split[1]; // 排序方式
            String filed = "";
            switch (type){
                case "2":
                    filed = "price";
                    break;
                default:
                    filed = "hotScore";
                    break;
            }
            Sort.Direction direction = Sort.Direction.ASC;
            try {
                direction = Sort.Direction.fromString(sort);
            }catch (Exception e){
            }
            dcl.addSort(Sort.by(direction,filed));
        }

        // 聚合分析
        // 返回当前商品含有的 品牌
        TermsAggregationBuilder tmIdAgg = AggregationBuilders.terms("tmIdAgg")
                .field("tmId").size(10);
        TermsAggregationBuilder tmNameAgg = AggregationBuilders.terms("tmNameAgg")
                .field("tmName").size(1);
        TermsAggregationBuilder tmLogoAgg = AggregationBuilders.terms("tmLogoUrlAgg")
                .field("tmLogoUrl").size(1);
        tmIdAgg.subAggregation(tmNameAgg);
        tmIdAgg.subAggregation(tmLogoAgg);
        dcl.addAggregation(tmIdAgg);
        // 属性聚合分析
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");
        // attrId聚合
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg")
                .field("attrs.attrId").size(20);
        attrAgg.subAggregation(attrIdAgg);
        // attrName聚合
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrNameAgg")
                .field("attrs.attrName").size(1);
        attrIdAgg.subAggregation(attrNameAgg);
        // attrValue聚合
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attrValueAgg")
                .field("attrs.attrValue").size(100);
        attrIdAgg.subAggregation(attrValueAgg);
        dcl.addAggregation(attrAgg);

        // 高亮查询
        if (! StringUtils.isEmpty(searchParamVo.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title").preTags("<span style='color:red'>").postTags("</span>");
            HighlightQuery highlightQuery = new HighlightQuery(highlightBuilder);
            dcl.setHighlightQuery(highlightQuery);
        }
        return dcl;
    }

    /**
     * 从es中删除一条商品
     * @param skuId
     */
    @Override
    public void deleteGoods(Long skuId) {
        goodsRepository.deleteById(skuId);
    }


    /**
     * 保存商品到es
     * @param goods
     */
    @Override
    public void saveGoods(Goods goods) {
        goodsRepository.save(goods);
    }

}
