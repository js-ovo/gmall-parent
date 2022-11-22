package com.jing.gmall.msg;

import lombok.Data;

@Data
public class OrderCreateMsg extends BaseMsg{
    private Long orderId;
    private Long userId;
}
