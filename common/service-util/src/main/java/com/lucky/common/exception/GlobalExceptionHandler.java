package com.lucky.common.exception;

import com.lucky.common.result.Result;
import com.lucky.common.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.AccessDeniedException;

/**
 * @author ChenYXCoding
 * @create 2023-03-09 16:45
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Result handleException(Throwable ex){
        // 控制台输出看一下，别消无声的
        ex.printStackTrace();
        // 返回错误信息
        return  Result.fail(ex.getMessage());
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        return Result.build(null, ResultCodeEnum.PERMISSION);
    }

}
