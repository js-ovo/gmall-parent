package com.jing.gmall.weball.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {


    @GetMapping("/auth/order")
    public String test(HttpServletRequest servletRequest){
        String uid = servletRequest.getHeader("uid");
        return "";
    }
}
