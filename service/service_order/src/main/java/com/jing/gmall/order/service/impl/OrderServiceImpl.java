package com.jing.gmall.order.service.impl;

import com.jing.gmall.cart.entity.CartInfo;
import com.jing.gmall.common.utils.HttpRequestUtils;
import com.jing.gmall.feignclients.cart.CartFeignClient;
import com.jing.gmall.feignclients.product.SkuFeignClient;
import com.jing.gmall.feignclients.user.UserFeignClient;
import com.jing.gmall.feignclients.ware.WareFeignClient;
import com.jing.gmall.order.vo.OrderConfirmVo;
import com.jing.gmall.order.service.OrderService;
import com.jing.gmall.order.vo.OrderSubmitVo;
import com.jing.gmall.user.entity.UserAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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
        orderConfirmVo.setTradeNo(tradeNo);

        return orderConfirmVo;
    }

    /**
     * 提交订单
     * @param tradeNo
     * @param orderSubmitVo
     * @return
     */
    @Override
    public Long submitOrder(String tradeNo, OrderSubmitVo orderSubmitVo) {
        return null;
    }
}
