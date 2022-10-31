package com.jing.gmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.gmall.product.entity.BaseAttrInfo;

import java.util.List;

/**
* @author Jing
* @description 针对表【base_attr_info(属性表)】的数据库操作Service
* @createDate 2022-10-30 18:14:38
*/
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    /**
     * 获取商品属性信息和值
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id);

    /**
     * 添加平台属性信息
     * @param baseAttrInfo
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

}
