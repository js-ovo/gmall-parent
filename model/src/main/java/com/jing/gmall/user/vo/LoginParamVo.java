package com.jing.gmall.user.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginParamVo {

    @JsonProperty("loginName")
    private String loginName;
    @JsonProperty("passwd")
    private String passwd;

}
