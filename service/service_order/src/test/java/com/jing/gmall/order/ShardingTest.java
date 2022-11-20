package com.jing.gmall.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jing.gmall.order.entity.OrderInfo;
import com.jing.gmall.order.mapper.OrderInfoMapper;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class ShardingTest {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Test
    void insertTest(){
        for (int i = 1; i < 10; i++) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setUserId((long) i);
            orderInfo.setConsignee("");
            orderInfo.setConsigneeTel("");
            orderInfo.setTotalAmount(new BigDecimal("0"));
            orderInfo.setOrderStatus("");
            orderInfo.setPaymentWay("");
            orderInfo.setDeliveryAddress("");
            orderInfo.setOrderComment("");
            orderInfo.setOutTradeNo("");
            orderInfo.setTradeBody("");
            orderInfo.setCreateTime(new Date());
            orderInfo.setExpireTime(new Date());
            orderInfo.setProcessStatus("");
            orderInfo.setTrackingNo("");
            orderInfo.setParentOrderId(0L);
            orderInfo.setImgUrl("");
            orderInfo.setProvinceId(0L);
            orderInfo.setOperateTime(new Date());
            orderInfo.setActivityReduceAmount(new BigDecimal("0"));
            orderInfo.setCouponAmount(new BigDecimal("0"));
            orderInfo.setOriginalTotalAmount(new BigDecimal("0"));
            orderInfo.setFeightFee(new BigDecimal("0"));
            orderInfo.setRefundableTime(new Date());
            orderInfoMapper.insert(orderInfo);
            System.out.println("=================================");
        }
    }

    @Test
    void selectTest(){
        // 强制主库路由 只能从主库读数据、

        HintManager instance = HintManager.getInstance();
        instance.setWriteRouteOnly();
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",2);
        List<OrderInfo> orderInfos = orderInfoMapper.selectList(wrapper);
        orderInfos.forEach(System.out::println);
        System.out.println("===============");
        orderInfoMapper.selectList(wrapper);
    }
}
