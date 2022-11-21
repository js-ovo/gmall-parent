package com.jing.gmall.common.result;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 *
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    ERROR(2013,"拒绝访问"),
    PAY_RUN(205, "支付中"),
    LOGIN_FAIL(207,"密码错误"),
    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),
    SECKILL_NO_START(210, "秒杀还没开始"),
    SECKILL_RUN(211, "正在排队中"),
    SECKILL_NO_PAY_ORDER(212, "您有未支付的订单"),
    SECKILL_FINISH(213, "已售罄"),
    SECKILL_END(214, "秒杀已结束"),
    SECKILL_SUCCESS(215, "抢单成功"),
    SECKILL_FAIL(216, "抢单失败"),
    SECKILL_ILLEGAL(217, "请求不合法"),
    SECKILL_ORDER_SUCCESS(218, "下单成功"),
    COUPON_GET(220, "优惠券已经领取"),
    COUPON_LIMIT_GET(221, "优惠券已发放完毕"),


    CART_IS_NULL(300,"购物车为空"),
    CART_NO_CHECKED(301,"未选中任何商品"),


    CART_ITEM_COUNT_FLOWER(302, "单个商品数量超过最大值"),
    CART_MAX_ITEM_COUNT(303, "超过购物车最大商品数"),
    ORDER_SUBMIT_REPEAT(1000,"订单重复提交，请刷新页面再试!"),
    SKU_NO_STOCK(1001,"请刷新页面，移除购物车中没有库存的商品，再提交订单!"),
    PRICE_CHANGED(1002,"商品价格发生了变化，请刷新页面重新确认，再提交")
    ;

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
