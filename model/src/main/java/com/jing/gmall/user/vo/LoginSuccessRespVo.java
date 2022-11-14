package com.jing.gmall.user.vo;

import lombok.Data;

/**
 * 用户登录成功返回的信息
 */
@Data
public class LoginSuccessRespVo {
    private String token;
    private String nickName;
    private String name;
}
