package com.jing.gmall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.product.entity.SpuImage;
import com.jing.gmall.product.entity.SpuInfo;
import com.jing.gmall.product.entity.SpuSaleAttr;
import com.jing.gmall.product.entity.SpuSaleAttrValue;
import com.jing.gmall.product.mapper.SpuInfoMapper;
import com.jing.gmall.product.service.SpuImageService;
import com.jing.gmall.product.service.SpuInfoService;
import com.jing.gmall.product.service.SpuSaleAttrService;
import com.jing.gmall.product.service.SpuSaleAttrValueService;
import com.jing.gmall.product.vo.SpuInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Jing
* @description 针对表【spu_info(商品表)】的数据库操作Service实现
* @createDate 2022-10-30 18:14:38
*/
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService{

    @Autowired
    private SpuImageService spuImageService;
    @Autowired
    SpuSaleAttrService spuSaleAttrService;
    @Autowired
    SpuSaleAttrValueService spuSaleAttrValueService;

    /**
     * 保存spu
     * @param spuInfoVo
     */
    @Override
    @Transactional
    public void saveSpuInfo(SpuInfoVo spuInfoVo) {
        // 保存到spu_info表中
        SpuInfo spuInfo = new SpuInfo();
        BeanUtils.copyProperties(spuInfoVo,spuInfo);
        this.save(spuInfo);

        Long spuId = spuInfo.getId();

        // 保存到spu_Image中
        List<SpuInfoVo.SpuImageListDTO> spuImageList = spuInfoVo.getSpuImageList();
        List<SpuImage> spuImages = spuImageList.stream().map(spuImageDto -> {
            SpuImage spuImage = new SpuImage();
            BeanUtils.copyProperties(spuImageDto, spuImage);
            spuImage.setSpuId(spuId);
            return spuImage;
        }).collect(Collectors.toList());
        spuImageService.saveBatch(spuImages);
        // 销售属性信息保存到 spu_sale_attr

        List<SpuInfoVo.SpuSaleAttrListDTO> spuSaleAttrListDTOS = spuInfoVo.getSpuSaleAttrList();

        List<SpuSaleAttr> spuSaleAttrs = new ArrayList<>();
        List<SpuSaleAttrValue> spuSaleAttrValues = new ArrayList<>();
        spuSaleAttrListDTOS.forEach(spuSaleAttrDto -> {
            SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
            BeanUtils.copyProperties(spuSaleAttrDto, spuSaleAttr);
            spuSaleAttr.setSpuId(spuId);
            //销售属性信息保存到 spu_sale_attr
            spuSaleAttrs.add(spuSaleAttr);

            //销售属性信息的值保存到 spu_sale_attr_value
            spuSaleAttrDto.getSpuSaleAttrValueList().forEach(spuSaleAttrValueDTO -> {
                SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
                BeanUtils.copyProperties(spuSaleAttrValueDTO,spuSaleAttrValue);
                spuSaleAttrValue.setSpuId(spuId);
                spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                spuSaleAttrValues.add(spuSaleAttrValue);
            });
        });
        // 保存
        spuSaleAttrService.saveBatch(spuSaleAttrs);
        spuSaleAttrValueService.saveBatch(spuSaleAttrValues);

    }
}




