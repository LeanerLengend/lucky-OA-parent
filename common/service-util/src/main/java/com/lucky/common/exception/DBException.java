package com.lucky.common.exception;


import lombok.Data;

/**
 * @author ChenYXCoding
 * @create 2023-03-09 19:47
 */
@Data
public class DBException extends RuntimeException{

    private Integer code;

    public DBException(String message) {
        super(message);
        this.code = 201;
    }

}
