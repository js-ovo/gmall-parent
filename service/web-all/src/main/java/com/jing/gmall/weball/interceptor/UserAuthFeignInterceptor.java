package com.jing.gmall.weball.interceptor;

import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.weball.controller.CartController;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Component
public class UserAuthFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        log.info("执行拦截器向请求模板中添加请求头！");
        HttpServletRequest request = CartController.threadInfo.get(Thread.currentThread());
        String tempId = request.getHeader(RedisConst.TEMP_ID);
        String uid = request.getHeader(RedisConst.USER_UID);
        template.header(RedisConst.TEMP_ID,tempId);
        template.header(RedisConst.USER_UID,uid);
    }

}
