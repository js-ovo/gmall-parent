package com.jing.gmall.search.service;

import com.jing.gmall.search.entity.Goods;
import com.jing.gmall.search.vo.SearchParamVo;
import com.jing.gmall.search.vo.SearchRespVo;

public interface SearchService {
    SearchRespVo search(SearchParamVo searchParamVo);

    void deleteGoods(Long skuId);

    void saveGoods(Goods goods);

    void updateHotScore(Long skuId, Long hotScore);
}
