package com.jing.gmall.weball.controller;


import com.jing.gmall.feignclients.product.CategoryFeignClient;
import com.jing.gmall.weball.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexPageController {


    @Autowired
    private CategoryFeignClient categoryFeignClient;


    @GetMapping("/")
    public String index(Model model){
        List<CategoryVo> vos = categoryFeignClient.getCategoryTreeData().getData();
        model.addAttribute("list",vos);
        return "index/index";
    }
}
