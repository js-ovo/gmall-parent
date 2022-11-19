package com.jing.gmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jing.gmall.order.entity.PaymentInfo;
import com.jing.gmall.order.service.PaymentInfoService;
import com.jing.gmall.order.mapper.PaymentInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Jing
* @description 针对表【payment_info(支付信息表)】的数据库操作Service实现
* @createDate 2022-11-19 16:24:12
*/
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo>
    implements PaymentInfoService{

}




