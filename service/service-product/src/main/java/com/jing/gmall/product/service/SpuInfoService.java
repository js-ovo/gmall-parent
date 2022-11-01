package com.jing.gmall.product.service;

import com.jing.gmall.product.entity.SpuInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.gmall.product.vo.SpuInfoVo;

/**
* @author Jing
* @description 针对表【spu_info(商品表)】的数据库操作Service
* @createDate 2022-10-30 18:14:38
*/
public interface SpuInfoService extends IService<SpuInfo> {

    void saveSpuInfo(SpuInfoVo spuInfoVo);

}
