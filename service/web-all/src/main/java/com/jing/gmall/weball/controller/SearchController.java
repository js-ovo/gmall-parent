package com.jing.gmall.weball.controller;

import com.jing.gmall.feignclients.search.SearchFeignClient;
import com.jing.gmall.search.vo.SearchParamVo;
import com.jing.gmall.search.vo.SearchRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 搜索
 */
@Controller
public class SearchController {


    @Autowired
    private SearchFeignClient searchFeignClient;

    /**
     * 输入商品的检索参数进行检索
     * @param searchParamVo  检索条件vo
     * @return
     */
    @GetMapping("/list.html")
    public String searchList(SearchParamVo searchParamVo, Model model){

        //1 接收检索条件
        //2 调用检索服务进行检索,返回数据
        //TODO 远程调用
        SearchRespVo respVo = searchFeignClient.searchGoods(searchParamVo).getData();
        //SearchRespVo searchRespVo = new SearchRespVo();

        //3 拿到结果渲染
        model.addAttribute("searchParam",respVo.getSearchParam());
        model.addAttribute("trademarkParam",respVo.getTrademarkParam());
        model.addAttribute("propsParamList",respVo.getPropsParamList());
        model.addAttribute("trademarkList",respVo.getTrademarkList());
        model.addAttribute("attrsList",respVo.getAttrsList());
        model.addAttribute("urlParam",respVo.getUrlParam());
        model.addAttribute("orderMap",respVo.getOrderMap());
        model.addAttribute("goodsList",respVo.getGoodsList());
        model.addAttribute("pageNo",respVo.getPageNo());
        model.addAttribute("totalPages",respVo.getTotalPages());

        return "list/index";
    }
}
