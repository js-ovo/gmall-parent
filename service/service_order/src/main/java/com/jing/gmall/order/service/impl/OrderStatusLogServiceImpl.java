package com.jing.gmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.order.entity.OrderStatusLog;
import com.jing.gmall.order.service.OrderStatusLogService;
import com.jing.gmall.order.mapper.OrderStatusLogMapper;
import org.springframework.stereotype.Service;

/**
* @author Jing
* @description 针对表【order_status_log】的数据库操作Service实现
* @createDate 2022-11-19 16:24:12
*/
@Service
public class OrderStatusLogServiceImpl extends ServiceImpl<OrderStatusLogMapper, OrderStatusLog>
    implements OrderStatusLogService{

}




