package com.jing.gmall.order.vo;

import lombok.Data;

import java.util.List;

/**
 * 接收 库存端发送的拆分订单参数
 */
@Data
public class SkuWareMapVo {
    private Long wareId;
    private List<Long> skuIds;
}
