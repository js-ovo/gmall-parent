package com.jing.gmall.feignclients.user;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.user.entity.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("service-user")
@RequestMapping("/api/inner/rpc/user")
public interface UserFeignClient {
    /**
     * 获取用户地址信息列表
     * @return
     */
    @GetMapping("/getUserAddress")
    Result<List<UserAddress>> getUserAddress();
}
