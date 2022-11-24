package com.jing.gmall.order.mq.listener;

import com.jing.gmall.common.config.mq.service.MqService;
import com.jing.gmall.common.constant.MqConst;
import com.jing.gmall.msg.OrderPayedMsg;
import com.jing.gmall.msg.WareStockMsg;
import com.jing.gmall.order.entity.OrderDetail;
import com.jing.gmall.order.entity.OrderInfo;
import com.jing.gmall.order.service.OrderDetailService;
import com.jing.gmall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderPaySuccessListener {

    @Autowired
    private MqService mqService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;



    @RabbitListener(queues = MqConst.ORDER_PAYED_QUEUE)
    public void orderPayedSuccess(Message message, Channel channel){
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            OrderPayedMsg payedMsg = mqService.getContent(message, OrderPayedMsg.class);
            // 获取订单信息
            OrderInfo orderInfo = orderService.getOrderInfo(payedMsg.getOutTradeNo());
            // 修改 订单状态为已经支付
            orderService.updatePayedOrder(orderInfo.getId(),orderInfo.getUserId());
            // 向 库存系统发送消息 更新库存信息
            WareStockMsg stockMsg = getWareStockMsg(orderInfo);
            mqService.convertAndSend(MqConst.WARE_STOCK_EXCHANGE,MqConst.WARE_STOCK_RK,stockMsg);
            log.info("向库存系统发送更新库存信息完成。。。。");
            channel.basicAck(deliveryTag,false);
        } catch (Exception e){
            log.error("接收消息出错正在重试.....");
            mqService.retry(message,channel,3L);
        }

    }

    /**
     * 封装向库存系统发送的消息
     * @param orderInfo
     * @return
     */
    private WareStockMsg getWareStockMsg(OrderInfo orderInfo) {
        WareStockMsg stockMsg = new WareStockMsg();
        stockMsg.setOrderId(orderInfo.getId());
        stockMsg.setConsignee(orderInfo.getConsignee());
        stockMsg.setConsigneeTel(orderInfo.getConsigneeTel());
        stockMsg.setOrderComment(orderInfo.getOrderComment());
        stockMsg.setOrderBody(orderInfo.getTradeBody());
        stockMsg.setDeliveryAddress(orderInfo.getDeliveryAddress());

        List<OrderDetail> orderDetails = orderDetailService.getOrderDetails(orderInfo.getId(), orderInfo.getUserId());

        List<WareStockMsg.Details> details = orderDetails.stream().map(item -> {
            WareStockMsg.Details detail = new WareStockMsg.Details();
            detail.setSkuId(item.getSkuId());
            detail.setSkuName(item.getSkuName());
            detail.setSkuNum(item.getSkuNum());
            return detail;
        }).collect(Collectors.toList());

        stockMsg.setDetails(details);
        return stockMsg;
    }
}
