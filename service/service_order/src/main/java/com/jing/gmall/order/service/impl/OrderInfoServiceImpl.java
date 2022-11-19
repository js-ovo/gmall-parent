package com.jing.gmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.order.entity.OrderInfo;
import com.jing.gmall.order.service.OrderInfoService;
import com.jing.gmall.order.mapper.OrderInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Jing
* @description 针对表【order_info(订单表 订单表)】的数据库操作Service实现
* @createDate 2022-11-19 16:24:12
*/
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
    implements OrderInfoService{

}




