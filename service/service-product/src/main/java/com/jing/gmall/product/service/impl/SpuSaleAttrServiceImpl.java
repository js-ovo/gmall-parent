package com.jing.gmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.product.entity.SpuSaleAttr;
import com.jing.gmall.product.service.SpuSaleAttrService;
import com.jing.gmall.product.mapper.SpuSaleAttrMapper;
import com.jing.gmall.product.vo.SkuAttrValueVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author Jing
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service实现
* @createDate 2022-10-30 18:14:38
*/
@Service
@Slf4j
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr>
    implements SpuSaleAttrService{

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    /**
     * 获取销售属性对应的值
     * @param spuId
     * @return
     */
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {
        return spuSaleAttrMapper.getSpuSaleAttrList(spuId);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrAndValueListWithOrder(Long spuId,Long skuId) {
        return spuSaleAttrMapper.getSpuSaleAttrAndValueListWithOrder(spuId,skuId);
    }

    @Override
    public String getSkuAttrValueJson(Long spuId) {
        List<SkuAttrValueVo> attrValueVos = spuSaleAttrMapper.getSkuAttrValueJson(spuId);
        log.info("attrValueVos: {}",attrValueVos);
        Map<String, Long> map =
                attrValueVos.stream().collect(Collectors.toMap(SkuAttrValueVo::getVal, SkuAttrValueVo::getSkuId));
        log.info("map: {}",map);
        return JSON.toJSONString(map);
    }
}




