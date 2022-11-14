package com.jing.gmall.weball.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 控制登录页面跳转
 */
@Controller
public class LoginController {


    /**
     * 跳转到登录页面
     * @param originUrl
     * @param model
     * @return
     */
    @GetMapping("/login.html")
    public String login(@RequestParam("originUrl") String originUrl, Model model){
        model.addAttribute("originUrl",originUrl);
        return "login";
    }
}
