package com.jing.gmall.seckill.mq;

import com.jing.gmall.common.config.mq.service.MqService;
import com.jing.gmall.common.constant.MqConst;
import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.common.util.DateUtil;
import com.jing.gmall.common.util.Jsons;
import com.jing.gmall.msg.SeckillQueueMsg;
import com.jing.gmall.order.entity.OrderDetail;
import com.jing.gmall.order.entity.OrderInfo;
import com.jing.gmall.seckill.entity.SeckillGoods;
import com.jing.gmall.seckill.service.SeckillGoodsService;
import com.jing.gmall.seckill.service.SeckillService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// 监听 秒杀下单扣减库存队列
@Component
@Slf4j
public class SeckillQueueListener {

    @Autowired
    private MqService mqService;
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(
            bindings = {
               @QueueBinding(value = @Queue(value = MqConst.SECKILL_QUEUE_QUEUE
                       ,durable = "true",autoDelete = "false",exclusive = "false")
               ,exchange = @Exchange(value = MqConst.SECKILL_EVENT_EXCHANGE,durable = "true",autoDelete = "false"),
               key = MqConst.SECKILL_QUEUE_RK)
            }
    )
    public void seckillQueue(Message message, Channel channel){
        log.info("收到库存扣减消息,准备开始扣减库存!");
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            SeckillQueueMsg queueMsg = mqService.getContent(message, SeckillQueueMsg.class);
            Long skuId = queueMsg.getSkuId();
            String seckillCode = queueMsg.getSeckillCode();
            Long userId = queueMsg.getUserId();
            // 扣数据库 库存
            // 获取 要扣减库存的秒杀商品信息
            SeckillGoods seckillDetail = seckillService.getSeckillDetail(skuId);
            // 将 秒杀商品封装到一个订单中
            OrderInfo orderInfo = prepareOrderInfo(userId,seckillDetail);
            // 扣减库存
            boolean result = seckillGoodsService.decrementStock(seckillDetail.getId());
            if (result){
                // 库存扣减成功  redis保存临时数据
                stringRedisTemplate.opsForValue().set(RedisConst.SECKILL_ORDER_KEY + seckillCode, Jsons.toJsonStr(orderInfo));
                // 更新redis中商品库存数据  重新上架商品信息到redis中
                String date = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
                seckillService.upSeckillGoos(date);
            } else {
                // 库存扣减失败
                stringRedisTemplate.opsForValue().set(RedisConst.SECKILL_ORDER_KEY + seckillCode,"false",2, TimeUnit.DAYS);
            }

            channel.basicAck(deliveryTag,false);
        } catch (Exception e){
            mqService.retry(message,channel,3L);
        }

    }

    private OrderInfo prepareOrderInfo(Long userId, SeckillGoods seckillDetail) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);

        //订单金额
        orderInfo.setTotalAmount(seckillDetail.getCostPrice());
        // 优惠金额
        orderInfo.setActivityReduceAmount(seckillDetail.getPrice().subtract(seckillDetail.getCostPrice()));
        orderInfo.setPaymentWay("ONLINE");


        // 购买的商品数据
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(0L);
        orderDetail.setUserId(userId);
        orderDetail.setSkuId(seckillDetail.getSkuId());
        orderDetail.setSkuName(seckillDetail.getSkuName());
        orderDetail.setImgUrl(seckillDetail.getSkuDefaultImg());

        // 商品原价
        orderDetail.setOrderPrice(seckillDetail.getPrice());
        orderDetail.setSkuNum(1);
        orderDetail.setCreateTime(new Date());
        orderDetail.setSplitTotalAmount(seckillDetail.getCostPrice());
        orderDetail.setSplitActivityAmount(seckillDetail.getPrice().subtract(seckillDetail.getCostPrice()));
        orderDetail.setSplitCouponAmount(new BigDecimal("0"));

        orderInfo.setOrderDetails(Collections.singletonList(orderDetail));
        return orderInfo;
    }
}
