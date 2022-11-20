package com.jing.gmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.user.entity.UserAddress;
import com.jing.gmall.user.mapper.UserAddressMapper;
import com.jing.gmall.user.service.UserAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Jing
* @description 针对表【user_address(用户地址表)】的数据库操作Service实现
* @createDate 2022-11-14 19:32:11
*/
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService {

    /**
     * 获取用户地址列表
     * @param userId
     * @return
     */
    @Override
    public List<UserAddress> getUserAddress(Long userId) {
        return this.list(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId, userId));
    }
}




