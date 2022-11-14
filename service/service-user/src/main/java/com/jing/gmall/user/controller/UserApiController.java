package com.jing.gmall.user.controller;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.common.result.ResultCodeEnum;
import com.jing.gmall.user.service.UserInfoService;
import com.jing.gmall.user.vo.LoginParamVo;
import com.jing.gmall.user.vo.LoginSuccessRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/passport")
public class UserApiController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginParamVo loginParamVo){

        // 返回 data.token code  data全部
        LoginSuccessRespVo respVo = userInfoService.login(loginParamVo);

        if (respVo == null){
            return Result.build("", ResultCodeEnum.LOGIN_FAIL);
        }
        return Result.ok(respVo);
    }

    /**
     * 根据token移除redis中的数据
     * @return
     */
    @GetMapping("/logout")
    public Result logout(@RequestHeader("token") String token){
        userInfoService.logout(token);
        return Result.ok();
    }
}
