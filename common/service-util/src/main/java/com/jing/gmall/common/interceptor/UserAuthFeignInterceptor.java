package com.jing.gmall.common.interceptor;

import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.common.utils.HttpRequestUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 *  feign远程调用拦截器 用来透传 用户id以及token
 */
@Slf4j
@Component
public class UserAuthFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {


        log.info("执行拦截器向请求模板中添加请求头！");
        template.header(RedisConst.TEMP_ID, HttpRequestUtils.getRequest().getHeader(RedisConst.TEMP_ID));
        template.header(RedisConst.USER_UID, HttpRequestUtils.getRequest().getHeader(RedisConst.USER_UID));

//        HttpServletRequest request = CartController.threadInfo.get(Thread.currentThread());
//        if (request != null) {
//            String tempId = request.getHeader(RedisConst.TEMP_ID);
//            String uid = request.getHeader(RedisConst.USER_UID);
//            template.header(RedisConst.TEMP_ID,tempId);
//            template.header(RedisConst.USER_UID,uid);
//        }

    }

}
