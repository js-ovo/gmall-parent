package com.jing.gmall.product.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SkuInfoVo implements Serializable {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("spuId")
    private Long spuId;
    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("skuName")
    private String skuName;
    @JsonProperty("weight")
    private BigDecimal weight;
    @JsonProperty("skuDesc")
    private String skuDesc;
    @JsonProperty("category3Id")
    private Long category3Id;
    @JsonProperty("skuDefaultImg")
    private String skuDefaultImg;
    @JsonProperty("tmId")
    private Long tmId;
    @JsonProperty("skuAttrValueList")
    private List<SkuAttrValueListDTO> skuAttrValueList;
    @JsonProperty("skuSaleAttrValueList")
    private List<SkuSaleAttrValueListDTO> skuSaleAttrValueList;
    @JsonProperty("skuImageList")
    private List<SkuImageListDTO> skuImageList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class SkuAttrValueListDTO implements Serializable {
        @JsonProperty("attrId")
        private Long attrId;
        @JsonProperty("valueId")
        private Long valueId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class SkuSaleAttrValueListDTO implements Serializable {
        @JsonProperty("saleAttrValueId")
        private Long saleAttrValueId;
        @JsonProperty("saleAttrValueName")
        private String saleAttrValueName;
        @JsonProperty("baseSaleAttrId")
        private Long baseSaleAttrId;
        @JsonProperty("saleAttrName")
        private String saleAttrName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class SkuImageListDTO implements Serializable {
        @JsonProperty("spuImgId")
        private Long spuImgId;
        @JsonProperty("imgName")
        private String imgName;
        @JsonProperty("imgUrl")
        private String imgUrl;
        @JsonProperty("isDefault")
        private Integer isDefault;
    }
}
