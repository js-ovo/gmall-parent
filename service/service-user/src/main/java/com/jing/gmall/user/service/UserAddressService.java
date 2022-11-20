package com.jing.gmall.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.gmall.user.entity.UserAddress;

import java.util.List;

/**
* @author Jing
* @description 针对表【user_address(用户地址表)】的数据库操作Service
* @createDate 2022-11-14 19:32:11
*/
public interface UserAddressService extends IService<UserAddress> {

    List<UserAddress> getUserAddress(Long userId);
}
