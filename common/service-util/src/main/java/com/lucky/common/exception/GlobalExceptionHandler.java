package com.lucky.common.exception;

import com.lucky.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ChenYXCoding
 * @create 2023-03-09 16:45
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Throwable ex){
        // 控制台输出看一下，别消无声的
        ex.printStackTrace();
        // 返回错误信息
        return  Result.fail(ex.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result handleArithmeticException(ArithmeticException ex){
        // 控制台输出看一下，别消无声的
        ex.printStackTrace();
        // 返回错误信息
        return  Result.fail("服务器数学运算异常！");
    }


    @ExceptionHandler(DBException.class)
    @ResponseBody
    public Result handleDBException(DBException ex){
        // 控制台输出看一下，别消无声的
        ex.printStackTrace();
        // 返回错误信息
        return  Result.fail();
    }

}
