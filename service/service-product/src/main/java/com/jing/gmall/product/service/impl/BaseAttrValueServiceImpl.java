package com.jing.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.product.entity.BaseAttrValue;
import com.jing.gmall.product.service.BaseAttrValueService;
import com.jing.gmall.product.mapper.BaseAttrValueMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Jing
* @description 针对表【base_attr_value(属性值表)】的数据库操作Service实现
* @createDate 2022-10-30 18:14:38
*/
@Service
public class BaseAttrValueServiceImpl extends ServiceImpl<BaseAttrValueMapper, BaseAttrValue>
    implements BaseAttrValueService{

    /**
     *根据平台属性ID获取平台属性对象数据
     * @param attrId
     * @return
     */
    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id",attrId);
        return this.list(wrapper);
    }

}




