package com.jing.gmall.msg;

import lombok.Data;

@Data
public class SeckillQueueMsg {
    private Long skuId;
    private Long userId;
    private String seckillCode;
}
