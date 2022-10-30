package com.jing.gmall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.product.entity.SkuInfo;
import com.jing.gmall.product.service.SkuInfoService;
import com.jing.gmall.product.mapper.SkuInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Jing
* @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
* @createDate 2022-10-30 18:14:38
*/
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{

}




