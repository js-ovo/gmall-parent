package com.jing.gmall.msg;

import lombok.Data;

/**
 * 更新库存后发送的消息
 */
@Data
public class WareOrderMsg extends BaseMsg{
    private Long orderId;
    private String status;
}
