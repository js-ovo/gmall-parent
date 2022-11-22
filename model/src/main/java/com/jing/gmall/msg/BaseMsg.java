package com.jing.gmall.msg;

import lombok.Data;

@Data
public class BaseMsg {
    private Long time = System.currentTimeMillis();
}
