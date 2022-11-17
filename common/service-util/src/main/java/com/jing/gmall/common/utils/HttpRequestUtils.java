package com.jing.gmall.common.utils;

import com.jing.gmall.common.constant.RedisConst;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取SpringMvc 利用监听器 给容器中放入的当前线程的请求
 */
public class HttpRequestUtils{



    public static HttpServletRequest getRequest(){
        // 获取springMvc利用监听器给当前线程放入的请求
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return requestAttributes.getRequest();
    }



    public static String getTempId(){
        HttpServletRequest request = getRequest();
        return request.getHeader(RedisConst.TEMP_ID);
    }


    public static Long getUserId(){
        String userId = getRequest().getHeader(RedisConst.USER_UID);
        if (userId == null || StringUtils.isEmpty(userId)){
            return null;
        }
        return Long.parseLong(userId);
    }



    private HttpRequestUtils(){}
}
