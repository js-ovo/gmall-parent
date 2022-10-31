package com.jing.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.product.entity.BaseAttrInfo;
import com.jing.gmall.product.entity.BaseAttrValue;
import com.jing.gmall.product.mapper.BaseAttrInfoMapper;
import com.jing.gmall.product.mapper.BaseAttrValueMapper;
import com.jing.gmall.product.service.BaseAttrInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author Jing
* @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
* @createDate 2022-10-30 18:14:38
*/
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService{

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;
    @Override
    public List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        return baseAttrInfoMapper.getAttrInfoList(category1Id,category2Id,category3Id);
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {

        if (baseAttrInfo.getId() == null) {
            // 保存属性信息
            addAttrInfo(baseAttrInfo);
        } else {
            // 修改属性信息
            updateAttrInfo(baseAttrInfo);
        }
    }

    private void updateAttrInfo(BaseAttrInfo baseAttrInfo) {
        // 修改base_attr_info中的信息
        baseAttrInfoMapper.updateById(baseAttrInfo);
        // 修改base_attr_value中的信息
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        // 获取所有的id
        List<Long> ids = attrValueList.stream()
                .map(BaseAttrValue::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // 删除
        QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id",baseAttrInfo.getId());
        if (ids.size() > 0){
            wrapper.notIn("id",ids);
        }
        baseAttrValueMapper.delete(wrapper);


        attrValueList.forEach(attrValue -> {
            // 修改
            if (attrValue.getId() != null){
                baseAttrValueMapper.updateById(attrValue);
                // 新增
            } else {
                attrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(attrValue);
            }
        });
    }

    private void addAttrInfo(BaseAttrInfo baseAttrInfo) {
        //保存属性信息到base_attr_info
        int insert = baseAttrInfoMapper.insert(baseAttrInfo);
        //保存属性信息到base_attr_value
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        attrValueList.forEach(attrValue -> {
            attrValue.setAttrId(baseAttrInfo.getId());
            baseAttrValueMapper.insert(attrValue);
        });
    }

}




