package com.lucky.common.exception;

/**
 * @author ChenYXCoding
 * @create 2023-04-09 15:25
 */
public class UserOperateException extends RuntimeException{

    private Integer code;


    public UserOperateException(String message) {
        super(message);
        this.code = 201;
    }

    public UserOperateException(Integer code,String message) {
        super(message);
        this.code = code;
    }

}
