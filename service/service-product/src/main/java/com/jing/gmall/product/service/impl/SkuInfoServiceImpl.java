package com.jing.gmall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.product.entity.SkuAttrValue;
import com.jing.gmall.product.entity.SkuImage;
import com.jing.gmall.product.entity.SkuInfo;
import com.jing.gmall.product.entity.SkuSaleAttrValue;
import com.jing.gmall.product.mapper.SkuInfoMapper;
import com.jing.gmall.product.service.SkuAttrValueService;
import com.jing.gmall.product.service.SkuImageService;
import com.jing.gmall.product.service.SkuInfoService;
import com.jing.gmall.product.service.SkuSaleAttrValueService;
import com.jing.gmall.product.vo.SkuInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Jing
* @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
* @createDate 2022-10-30 18:14:38
*/
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{

    @Autowired
    private SkuImageService skuImageService;

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Transactional
    @Override
    public void saveSkuInfo(SkuInfoVo skuInfoVo) {
        // 保存 skuInfo到 sku_info表中
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo,skuInfo);
        this.save(skuInfo);

        // 返回skuId
        Long skuId = skuInfo.getId();
        // 保存 skuImage到 sku_image表中
        List<SkuInfoVo.SkuImageListDTO> skuImageDtoList = skuInfoVo.getSkuImageList();
        List<SkuImage> skuImages = skuImageDtoList.stream().map(skuImageDto -> {
            SkuImage skuImage = new SkuImage();
            BeanUtils.copyProperties(skuImageDto, skuImage);
            skuImage.setSkuId(skuId);
            return skuImage;
        }).collect(Collectors.toList());
        skuImageService.saveBatch(skuImages);

        // 保存 skuAttrValue 到 sku_attr_value表中
        List<SkuInfoVo.SkuAttrValueListDTO> skuAttrValueDtoList = skuInfoVo.getSkuAttrValueList();
        List<SkuAttrValue> skuAttrValues = skuAttrValueDtoList.stream().map(skuAttrValueDTO -> {
            SkuAttrValue skuAttrValue = new SkuAttrValue();
            BeanUtils.copyProperties(skuAttrValueDTO, skuAttrValue);
            skuAttrValue.setSkuId(skuId);
            return skuAttrValue;
        }).collect(Collectors.toList());
        skuAttrValueService.saveBatch(skuAttrValues);
        // 保存 skuSaleAttrValue 到 sku_sale_attr_value表中
        List<SkuInfoVo.SkuSaleAttrValueListDTO> skuSaleAttrValueDtoList = skuInfoVo.getSkuSaleAttrValueList();
        List<SkuSaleAttrValue> skuSaleAttrValues = skuSaleAttrValueDtoList.stream().map(skuSaleAttrValueDto -> {
            SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
            BeanUtils.copyProperties(skuSaleAttrValueDto, skuSaleAttrValue);
            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValue.setSpuId(skuInfoVo.getSpuId());
            return skuSaleAttrValue;
        }).collect(Collectors.toList());
        skuSaleAttrValueService.saveBatch(skuSaleAttrValues);
    }

    @Override
    public BigDecimal getRealtimePrice(Long skuId) {
        return baseMapper.getRealtimePrice(skuId);
    }

    @Override
    public List<Long> getSkuIds() {
        return this.baseMapper.getSkuIds();
    }
}




