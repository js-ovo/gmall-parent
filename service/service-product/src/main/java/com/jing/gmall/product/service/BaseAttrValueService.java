package com.jing.gmall.product.service;

import com.jing.gmall.product.entity.BaseAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Jing
* @description 针对表【base_attr_value(属性值表)】的数据库操作Service
* @createDate 2022-10-30 18:14:38
*/
public interface BaseAttrValueService extends IService<BaseAttrValue> {
    /**
     *根据平台属性ID获取平台属性对象数据
     * @param attrId
     * @return
     */
    public List<BaseAttrValue> getAttrValueList(Long attrId);
}
