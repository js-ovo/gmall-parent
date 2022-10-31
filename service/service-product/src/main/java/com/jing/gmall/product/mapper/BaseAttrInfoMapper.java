package com.jing.gmall.product.mapper;

import com.jing.gmall.product.entity.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Jing
* @description 针对表【base_attr_info(属性表)】的数据库操作Mapper
* @createDate 2022-10-30 18:14:38
* @Entity com.jing.gmall.product.entity.BaseAttrInfo
*/
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {

    /**
     * 获取平台属性信息
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    List<BaseAttrInfo> getAttrInfoList(@Param("category1Id") Long category1Id,
                                       @Param("category2Id") Long category2Id,
                                       @Param("category3Id") Long category3Id);
}




