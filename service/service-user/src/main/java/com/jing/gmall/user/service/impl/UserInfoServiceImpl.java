package com.jing.gmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.common.util.Jsons;
import com.jing.gmall.common.util.MD5;
import com.jing.gmall.user.entity.UserInfo;
import com.jing.gmall.user.mapper.UserInfoMapper;
import com.jing.gmall.user.service.UserInfoService;
import com.jing.gmall.user.vo.LoginParamVo;
import com.jing.gmall.user.vo.LoginSuccessRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
* @author Jing
* @description 针对表【user_info(用户表)】的数据库操作Service实现
* @createDate 2022-11-14 19:32:11
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 用户登录 并返回信息给前端
     * @param loginParamVo
     * @return
     */
    @Override
    public LoginSuccessRespVo login(LoginParamVo loginParamVo) {
        // 对密码进行MD5加密
        String password = MD5.encrypt(loginParamVo.getPasswd());

        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name",loginParamVo.getLoginName());
        wrapper.eq("passwd",password);
        UserInfo userInfo = getOne(wrapper);
        if (userInfo != null){
            LoginSuccessRespVo respVo = new LoginSuccessRespVo();
            // 生成token
            String token = UUID.randomUUID().toString().replace("-", "");
            respVo.setToken(token);
            respVo.setNickName(userInfo.getNickName());
            respVo.setName(userInfo.getName());

            // 将用户信息存入redis中 7天过期
            stringRedisTemplate.opsForValue().set(RedisConst.USER_LOGIN_KEY + token
                    ,Jsons.toJsonStr(userInfo),RedisConst.USER_AUTH_TTL, TimeUnit.DAYS);
            return respVo;
        }
        // 登录失败
        return null;
    }

    @Override
    public void logout(String token) {
        stringRedisTemplate.delete(RedisConst.USER_LOGIN_KEY + token);
    }
}




