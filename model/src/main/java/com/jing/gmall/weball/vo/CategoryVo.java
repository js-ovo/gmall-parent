package com.jing.gmall.weball.vo;

import lombok.Data;

import java.util.List;

@Data
public class CategoryVo {
    private Long categoryId;
    private String categoryName;
    private List<CategoryVo> categoryChild;
}
