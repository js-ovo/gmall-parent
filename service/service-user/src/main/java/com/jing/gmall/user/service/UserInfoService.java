package com.jing.gmall.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.gmall.user.entity.UserInfo;
import com.jing.gmall.user.vo.LoginParamVo;
import com.jing.gmall.user.vo.LoginSuccessRespVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author Jing
* @description 针对表【user_info(用户表)】的数据库操作Service
* @createDate 2022-11-14 19:32:11
*/
public interface UserInfoService extends IService<UserInfo> {

    LoginSuccessRespVo login(LoginParamVo loginParamVo, HttpServletRequest request);

    void logout(String token);
}
