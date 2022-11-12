package com.jing.gmall.search.service.impl;

import com.jing.gmall.search.entity.Goods;
import com.jing.gmall.search.repository.GoodsRepository;
import com.jing.gmall.search.service.SearchService;
import com.jing.gmall.search.vo.SearchParamVo;
import com.jing.gmall.search.vo.SearchRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public SearchRespVo search(SearchParamVo searchParamVo) {
        //TODO 搜索
        return null;
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
