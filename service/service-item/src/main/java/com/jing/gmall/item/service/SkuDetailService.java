package com.jing.gmall.item.service;

import com.jing.gmall.item.vo.SkuDetailVo;

public interface SkuDetailService {
    SkuDetailVo getSkuDetail(Long skuId);

    void updateHotScore(Long skuId);
}
