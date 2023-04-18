package com.lucky.common.result;

import lombok.Getter;

/**
 * @author ChenYXCoding
 * @create 2023-03-07 19:20
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),

    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限");

    private Integer code;

    private String message;

    // 枚举点出属性的时候，应该是通过括号里面的值，然后构造出来值
    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
