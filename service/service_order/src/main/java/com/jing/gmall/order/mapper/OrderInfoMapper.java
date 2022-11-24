package com.jing.gmall.order.mapper;

import com.jing.gmall.order.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Jing
* @description 针对表【order_info(订单表 订单表)】的数据库操作Mapper
* @createDate 2022-11-19 16:24:12
* @Entity com.jing.gmall.order.entity.OrderInfo
*/
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     *  将订单状态修改为期望的状态
     * @param userId 用户id
     * @param orderId 订单号
     * @param orderStatus 想要修改的订单状态
     * @param processStatus 想要修改的订单 进程状态
     * @param expectOrderStatus 期望原来订单状态
     * @param expectProcessStatus 期望原来的 订单进程状态
     */
    void updateOrderStatus(@Param("userId") Long userId, @Param("orderId") Long orderId,
                           @Param("orderStatus") String orderStatus, @Param("processStatus") String processStatus
            , @Param("expectOrderStatus") List<String> expectOrderStatus
            , @Param("expectProcessStatus") List<String> expectProcessStatus);
}




