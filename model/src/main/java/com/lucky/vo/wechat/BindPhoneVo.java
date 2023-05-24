package com.lucky.vo.wechat;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BindPhoneVo {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "openId不能为空")
    private String openId;
}
