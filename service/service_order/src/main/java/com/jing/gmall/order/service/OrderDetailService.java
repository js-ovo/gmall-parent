package com.jing.gmall.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jing.gmall.order.entity.OrderDetail;

import java.util.List;

/**
* @author Jing
* @description 针对表【order_detail(订单明细表)】的数据库操作Service
* @createDate 2022-11-19 16:24:12
*/
public interface OrderDetailService extends IService<OrderDetail> {

    List<OrderDetail> getOrderDetails(Long orderId, Long userId);
}
