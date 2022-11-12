package com.jing.gmall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.product.entity.SkuAttrValue;
import com.jing.gmall.product.service.SkuAttrValueService;
import com.jing.gmall.product.mapper.SkuAttrValueMapper;
import com.jing.gmall.search.entity.SearchAttr;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Jing
* @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Service实现
* @createDate 2022-10-30 18:14:38
*/
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue>
    implements SkuAttrValueService{


    @Override
    public List<SearchAttr> getSkuAttrNameAndValue(Long skuId) {
        return this.baseMapper.getSkuAttrNameAndValue(skuId);
    }
}




