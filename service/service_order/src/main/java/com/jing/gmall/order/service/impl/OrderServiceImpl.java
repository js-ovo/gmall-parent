package com.jing.gmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jing.gmall.cart.entity.CartInfo;
import com.jing.gmall.common.config.mq.service.MqService;
import com.jing.gmall.common.constant.MqConst;
import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.common.execption.GmallException;
import com.jing.gmall.common.result.ResultCodeEnum;
import com.jing.gmall.common.utils.HttpRequestUtils;
import com.jing.gmall.enums.OrderStatus;
import com.jing.gmall.enums.PaymentWay;
import com.jing.gmall.enums.ProcessStatus;
import com.jing.gmall.feignclients.cart.CartFeignClient;
import com.jing.gmall.feignclients.product.SkuFeignClient;
import com.jing.gmall.feignclients.user.UserFeignClient;
import com.jing.gmall.feignclients.ware.WareFeignClient;
import com.jing.gmall.msg.OrderCreateMsg;
import com.jing.gmall.order.entity.OrderDetail;
import com.jing.gmall.order.entity.OrderInfo;
import com.jing.gmall.order.entity.OrderStatusLog;
import com.jing.gmall.order.mapper.OrderInfoMapper;
import com.jing.gmall.order.mapper.OrderStatusLogMapper;
import com.jing.gmall.order.service.OrderDetailService;
import com.jing.gmall.order.service.OrderService;
import com.jing.gmall.order.vo.OrderConfirmVo;
import com.jing.gmall.order.vo.OrderSubmitVo;
import com.jing.gmall.user.entity.UserAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartFeignClient cartFeignClient;
    @Autowired
    private SkuFeignClient skuFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private WareFeignClient wareFeignClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderStatusLogMapper orderStatusLogMapper;
    @Autowired
    private MqService mqService;

    /**
     * 获取 订单提交页面的数据
     * @return
     */
    @Override
    public OrderConfirmVo getOrderConfirmData() {

        // 获取确认页数据
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();

        // 获取商品信息列表  所有选中的商品
        List<CartInfo> cartInfos = cartFeignClient.getCheckedCartInfo().getData();
        List<OrderConfirmVo.Product> products = cartInfos.stream().map(cartInfo -> {
            OrderConfirmVo.Product product = new OrderConfirmVo.Product();
            product.setSkuId(cartInfo.getSkuId());
            product.setSkuName(cartInfo.getSkuName());
            product.setSkuNum(cartInfo.getSkuNum());
            // 获取商品的实时价格
            BigDecimal price = skuFeignClient.getRealtimePrice(cartInfo.getSkuId()).getData();
            product.setOrderPrice(price);
            // 远程查询商品是否有库存
            String hasStock = wareFeignClient.hasStock(cartInfo.getSkuId(), cartInfo.getSkuNum());
            product.setHasStock(hasStock);
            product.setImgUrl(cartInfo.getImgUrl());
            return product;
        }).collect(Collectors.toList());
        orderConfirmVo.setDetailArrayList(products);

        // 获取总的商品数量
        Integer sum = products.stream()
                .map(OrderConfirmVo.Product::getSkuNum)
                .reduce(Integer::sum).get();

        orderConfirmVo.setTotalNum(sum);
        // 获去订单总金额
        BigDecimal totalAmount = products.stream()
                .map(item -> item.getOrderPrice().multiply(new BigDecimal(item.getSkuNum())))
                .reduce(BigDecimal::add).get();
        orderConfirmVo.setTotalAmount(totalAmount);
        // 获取 收获地址信息列表  远程调用用户服务
        List<UserAddress> userAddresses = userFeignClient.getUserAddress().getData();
        orderConfirmVo.setUserAddressList(userAddresses);
        // 生成 流水号
        String tradeNo = "GML_" + System.currentTimeMillis() + "_" + HttpRequestUtils.getUserId();
        // 将流水号 存入redis中 用来校验 防止重复提交
        stringRedisTemplate.opsForValue().set(RedisConst.ORDER_TOKEN + tradeNo,"1",30, TimeUnit.MINUTES);
        orderConfirmVo.setTradeNo(tradeNo);
        return orderConfirmVo;
    }

    /**
     * 提交订单
     * @param tradeNo
     * @param orderSubmitVo
     * @return
     */
    @Transactional
    @Override
    public Long submitOrder(String tradeNo, OrderSubmitVo orderSubmitVo) {

//        // 防止重复提交进行校验 第一次提交删除redis中的令牌
//        if (! stringRedisTemplate.hasKey(RedisConst.ORDER_TOKEN + tradeNo)){
//            // 不存在 已经删除了  订单重复提交了
//            throw new GmallException(ResultCodeEnum.ORDER_SUBMIT_REPEAT);
//        }
//        // 第一次提交 删除 信息
//        stringRedisTemplate.delete(RedisConst.ORDER_TOKEN + tradeNo);

        // 校验 和删除 原子操作
        String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                "then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";

        Long execute = stringRedisTemplate.execute(RedisScript.of(script, Long.class),
                Collections.singletonList(RedisConst.ORDER_TOKEN + tradeNo), "1");
        if (! execute.equals(1L)){
            throw new GmallException(ResultCodeEnum.ORDER_SUBMIT_REPEAT);
        }


        // 验证库存
        List<OrderSubmitVo.OrderDetailListDTO> orderDetailList = orderSubmitVo.getOrderDetailList();
        boolean hasStock = orderDetailList.stream()
                .anyMatch(item -> "0".equals(wareFeignClient.hasStock(item.getSkuId(), item.getSkuNum())));
        if (hasStock){
            throw new GmallException(ResultCodeEnum.SKU_NO_STOCK);
        }

        // 验证订单价格是否为最新的价格
        boolean needUpdatePrice = orderDetailList.stream()
                .anyMatch(item ->
                        Math.abs(item.getOrderPrice().subtract(skuFeignClient.getRealtimePrice(item.getSkuId()).getData()).doubleValue()) > 0.001
                );
        if (needUpdatePrice) {
            throw new GmallException(ResultCodeEnum.PRICE_CHANGED);
        }
        // 保存订单 信息相关数据
        // orderInfo 表保存数据
        OrderInfo orderInfo = prepareOrderInfo(tradeNo, orderSubmitVo);
        orderInfoMapper.insert(orderInfo);
        // order_detail 保存订单明细
        List<OrderDetail> orderDetails = prepareOrderDetails(orderInfo,orderSubmitVo);
        orderDetailService.saveBatch(orderDetails);
        // order_status_log 保存订单状态
        OrderStatusLog orderStatusLog = prepareOrderStatusLog(orderInfo);
        orderStatusLogMapper.insert(orderStatusLog);

        // 删除购物车中选中的数据
        List<Long> ids = orderDetailList.stream()
                .map(OrderSubmitVo.OrderDetailListDTO::getSkuId).collect(Collectors.toList());
        cartFeignClient.removeSkuByIds(ids);

        //超过规定时间内未支付订单 关闭订单  向消息队列中发送关闭订单的消息
        OrderCreateMsg orderCreateMsg = new OrderCreateMsg();
        orderCreateMsg.setUserId(HttpRequestUtils.getUserId());
        orderCreateMsg.setOrderId(orderInfo.getId());
        mqService.convertAndSend(MqConst.ORDER_EVENT_EXCHANGE,MqConst.ORDER_CREATE_RK,orderCreateMsg);
        return orderInfo.getId();
    }


    /**
     * 获取订单信息
     * @param userId
     * @param orderId
     * @return
     */
    @Override
    public OrderInfo getOrderInfo(Long userId, Long orderId) {
        if (userId == null){
            return null;
        }
        return orderInfoMapper
                .selectOne(
                        new LambdaQueryWrapper<OrderInfo>()
                                .eq(OrderInfo::getId, orderId)
                                .eq(OrderInfo::getUserId, userId));
    }

    /**
     * 关闭订单
     * @param userId 用户id
     * @param orderId 订单
     */
    @Override
    public void closeOrder(Long userId, Long orderId) {
        orderInfoMapper.updateOrderStatus(userId,orderId,
                ProcessStatus.CLOSED.getOrderStatus().name(),ProcessStatus.CLOSED.name()
                ,OrderStatus.UNPAID.name(),ProcessStatus.UNPAID.name());
    }

    /**
     * 封装 订单状态
     * @param orderInfo
     * @return
     */
    private OrderStatusLog prepareOrderStatusLog(OrderInfo orderInfo) {
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        orderStatusLog.setUserId(orderInfo.getUserId());
        orderStatusLog.setOrderId(orderInfo.getId());
        orderStatusLog.setOrderStatus(orderInfo.getOrderStatus());
        orderStatusLog.setOperateTime(new Date());
        return orderStatusLog;
    }

    /**
     * 封装 order_detail相关实体信息
     * @param orderInfo
     * @param orderSubmitVo
     * @return
     */
    private List<OrderDetail> prepareOrderDetails(OrderInfo orderInfo, OrderSubmitVo orderSubmitVo) {

        List<OrderDetail> orderDetails = orderSubmitVo.getOrderDetailList().stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderInfo.getId());
            orderDetail.setUserId(orderInfo.getUserId());
            orderDetail.setSkuId(item.getSkuId());
            orderDetail.setSkuName(item.getSkuName());
            orderDetail.setImgUrl(item.getImgUrl());
            orderDetail.setOrderPrice(item.getOrderPrice());
            orderDetail.setSkuNum(item.getSkuNum());
            orderDetail.setCreateTime(new Date());
            orderDetail.setSplitTotalAmount(item.getOrderPrice().multiply(new BigDecimal(item.getSkuNum())));
            orderDetail.setSplitActivityAmount(new BigDecimal("0"));
            orderDetail.setSplitCouponAmount(new BigDecimal("0"));
            return orderDetail;
        }).collect(Collectors.toList());
        return orderDetails;
    }

    /**
     * 封装存入 order_info 相关数据
     * @param tradeNo
     * @param orderSubmitVo
     * @return
     */
    private OrderInfo prepareOrderInfo(String tradeNo, OrderSubmitVo orderSubmitVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(HttpRequestUtils.getUserId());
        orderInfo.setConsignee(orderSubmitVo.getConsignee());
        orderInfo.setConsigneeTel(orderSubmitVo.getConsigneeTel());

        List<OrderSubmitVo.OrderDetailListDTO> orderDetailList = orderSubmitVo.getOrderDetailList();
        // 计算订单总的价格
        BigDecimal totalAmount = orderDetailList.stream().map(item -> item.getOrderPrice().multiply(new BigDecimal(item.getSkuNum())))
                .reduce(BigDecimal::add).get();
        orderInfo.setTotalAmount(totalAmount);
        // 订单状态
        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());
        // 支付方式
        orderInfo.setPaymentWay(PaymentWay.ONLINE.name());
        // 送货地址
        orderInfo.setDeliveryAddress(orderSubmitVo.getDeliveryAddress());
        // 订单备注
        orderInfo.setOrderComment(orderSubmitVo.getOrderComment());
        // 订单流水号
        orderInfo.setOutTradeNo(tradeNo);
        //订单体
        String tradeBody = orderDetailList.get(0).getSkuName();
        orderInfo.setTradeBody("GMALL_订单-" + tradeBody);
        //创建时间
        orderInfo.setCreateTime(new Date());
        //订单过期时间 30min 过期关闭订单
        orderInfo.setExpireTime(new Date(System.currentTimeMillis() + RedisConst.ORDER_TIME_OUT));
        // 订单进度状态
        orderInfo.setProcessStatus(ProcessStatus.UNPAID.name());
        orderInfo.setTrackingNo("");
        // 订单图片
        orderInfo.setImgUrl(orderDetailList.get(0).getImgUrl());
        // 操作时间
        orderInfo.setOperateTime(new Date());
        // 促销金额  远程调用获取优惠金额
        orderInfo.setActivityReduceAmount(new BigDecimal("0"));
        orderInfo.setCouponAmount(new BigDecimal("0"));

        // 原价
        orderInfo.setOriginalTotalAmount(totalAmount);
        // 运费 == 对接第三方 快递系统
        orderInfo.setFeightFee(new BigDecimal("0"));
        return orderInfo;
    }
}
