package com.jing.gmall.gateway.filter;

import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.common.result.Result;
import com.jing.gmall.common.result.ResultCodeEnum;
import com.jing.gmall.common.util.IpUtil;
import com.jing.gmall.common.util.Jsons;
import com.jing.gmall.gateway.properties.AuthUrlProperties;
import com.jing.gmall.user.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * 用户透传 进行统一鉴权验证处理
 */
@Slf4j
@Component
public class UserAuthGlobalFilter implements GlobalFilter {


    @Autowired
    private AuthUrlProperties authUrlProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求信息
        ServerHttpRequest request = exchange.getRequest();
        // 获取请求地址
        String path = request.getPath().toString();
        // 静态资源都直接放行不做处理
        if (matchRule(authUrlProperties.getNoAuthUrl(),path)) {
            return chain.filter(exchange);
        }
        // 内部远程调用的请求不对外暴露 统一拒绝访问
        if (matchRule(authUrlProperties.getDenyUrl(),path)) {
            // 设置返回信息
            Result<String> result = Result.build("访问错误", ResultCodeEnum.ERROR);
            return responseJson(Jsons.toJsonStr(result),exchange);
        }
        // 判断需要登录的请求
        // 拿到token 从请求头中获取
        String token = getHeard("token",request);
        if (matchRule(authUrlProperties.getAuthUrl(),path)) {
            // token是否是空值  以及 redis中是否有记录
            if (StringUtils.isEmpty(token) || !isLogin(token)) {
                // token 为空 或者 token 在redis中没有记录 非法登录 直接返回
                return redirectUrl(exchange,authUrlProperties.getLoginPage());
            }
        }
        // 正常请求
        // 1、正常请求带了token
        // 2、正常请求没有携带token
        // 3、正常请求携带错误的token

        // 登录请求
        // 登录请求带了 token 并且在redis中有记录
        // 验证登录请求  redis中有token 说明是对应的token
        UserInfo userInfo = null;
        if (!StringUtils.isEmpty(token)) {
            // 普通请求带了 token 还是错误的token
            if (!isLogin(token)) {
                return redirectUrl(exchange,authUrlProperties.getLoginPage());
            }
            // 需要登录验证的请求 或者普通请求携带了正确的token
            userInfo = getUserInfo(token);
            // redis中用户信息过期了
            if (userInfo == null){
                return redirectUrl(exchange,authUrlProperties.getLoginPage());
            }
            // 信息不合法
            if (! userInfo.getIp().equals(IpUtil.getGatwayIpAddress(request))) {
                return redirectUrl(exchange,authUrlProperties.getLoginPage());
            }
        }

        // TODO  id透传
        return doChain(exchange,chain,userInfo);
    }


    /**
     * 用户信息透传  userid 或者 tempId
     * @param exchange
     * @param chain
     * @param userInfo
     * @return
     */
    private Mono<Void> doChain(ServerWebExchange exchange, GatewayFilterChain chain, UserInfo userInfo) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 用户信息不为null 透传
        if (userInfo != null) {
            request.mutate().header(RedisConst.USER_UID,userInfo.getId().toString());
        }
        // 获取临时id
        String tempId = getHeard("userTempId", request);
        if (!StringUtils.isEmpty(tempId)){
            request.mutate().header(RedisConst.TEMP_ID,tempId);
        }

        ServerWebExchange newExchange = exchange.mutate()
                .request(request)
                .response(response).build();

        return chain.filter(newExchange);
    }


    /**
     * 判断 redis中 是否有 该key
     * @param token
     * @return
     */
    private boolean isLogin(String token) {
        return stringRedisTemplate.hasKey(RedisConst.USER_LOGIN_KEY +token);
    }

    /**
     * 从redis中获取 用户信息 json字符串
     */
    private UserInfo getUserInfo(String token){
        String json = stringRedisTemplate.opsForValue().get(RedisConst.USER_LOGIN_KEY + token);
        return Jsons.json2Obj(json,UserInfo.class);
    }


    /**
     * 获取请求中的数据
     * @param key
     * @param request
     * @return
     */
    private String getHeard(String key, ServerHttpRequest request) {
        // 获取请求头
        HttpHeaders headers = request.getHeaders();
        // 从请求头中获取 key 对应的value
        String value = headers.getFirst(key);
        if (StringUtils.isEmpty(value)){
            // 从cookie中获取 key对应的value
            HttpCookie cookie = request.getCookies().getFirst(key);
            if (cookie != null){
                value = cookie.getValue();
            }
            return value;
        }
        return value;
    }


    /**
     * 向浏览器 写出拒绝信息
     * @param json
     * @param exchange
     * @return
     */
    private Mono<Void> responseJson(String json,ServerWebExchange exchange) {
        // 进行拦截，返回错误的信息  给浏览器写json
        ServerHttpResponse response = exchange.getResponse();
        DataBuffer dataBuffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        // 向浏览器制定写出字符集 以及方式
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 写出
        return response.writeWith(Mono.just(dataBuffer));
    }


    /**
     * 判断指定路径是否匹配设置的匹配规则
     * @param patterns 匹配的规则
     * @param path 路径
     * @return 如果匹配任意一个 返回true
     */
    private boolean matchRule(List<String> patterns,String path){
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return patterns.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private Mono<Void> redirectUrl(ServerWebExchange exchange,String redirectUrl){
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();

        String path = "";
        // 获取原始的URL地址
        try {
            path = request.getURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        redirectUrl = redirectUrl + "?originUrl=" + path;
        // 设置重定向状态码
        response.setStatusCode(HttpStatus.FOUND);
        // 在请求头设置要重定向的地址
        response.getHeaders().setLocation(URI.create(redirectUrl));

        // 清除所有的 token 信息

        response.addCookie(ResponseCookie.from("token","")
                .maxAge(0)
                .domain(".gmall.com")
                .path("/")
                .build());

        response.addCookie(ResponseCookie.from("userInfo","")
                .maxAge(0)
                .domain(".gmall.com")
                .path("/")
                .build());

        return response.setComplete();
    }
}
