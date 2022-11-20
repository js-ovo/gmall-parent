package com.jing.gmall.user.rpc;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.common.utils.HttpRequestUtils;
import com.jing.gmall.user.entity.UserAddress;
import com.jing.gmall.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/inner/rpc/user")
@RestController
public class UserRpcController {

    @Autowired
    private UserAddressService userAddressService;

    /**
     * 获取用户地址信息列表
     * @return
     */
    @GetMapping("/getUserAddress")
    public Result<List<UserAddress>> getUserAddress(){
        Long userId = HttpRequestUtils.getUserId();
        List<UserAddress> userAddresses =  userAddressService.getUserAddress(userId);
        return Result.ok(userAddresses);
    }
}
