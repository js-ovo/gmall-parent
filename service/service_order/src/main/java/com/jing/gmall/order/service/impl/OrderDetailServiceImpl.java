package com.jing.gmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.order.entity.OrderDetail;
import com.jing.gmall.order.service.OrderDetailService;
import com.jing.gmall.order.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Jing
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-11-19 16:24:12
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

    @Override
    public List<OrderDetail> getOrderDetails(Long orderId, Long userId) {
        return this.list(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getOrderId,orderId)
                .eq(OrderDetail::getUserId,userId));
    }
}




