package com.jing.gmall.search;

import com.jing.gmall.search.service.SearchService;
import com.jing.gmall.search.vo.SearchParamVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SearchTest {

    @Autowired
    SearchService searchService;

    @Test
    public void test(){
        SearchParamVo vo = new SearchParamVo();
        vo.setCategory3Id(61L);
        vo.setTrademark("4:小米");
        vo.setPageNo(0);
        searchService.search(vo);
    }
}
