package com.lucky.common.result;

import lombok.Data;

/**
 * 创建结果
 * <ul>
 *     <li>build 自定义结果</li>
 *     <li><b>T</b> 返回结果类型</li>
 * </ul>
 * @author ChenYXCoding
 * @create 2023-03-07 19:14
 */
@Data
public class Result<T> {

    private int code;

    private String message;

    private T data;

    private boolean ok;

    private Result(){
        // 私有化构造器，其他人不能new出来，
    }

    /**
     * 自定义构建返回数据
     * @return 结果
     */
    private static<T> Result build(T data){
        Result<T> result = new Result<T>();
        if(data != null){
            result.setData(data);
        }
        return result;
    }

    public static<T> Result build(T data,Integer code,String message ){
        Result<T> result = Result.build(data);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static<T> Result build(T data,ResultCodeEnum resultCodeEnum ){
        Result result = Result.build(data);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    /**
     * 返回结果为空，正确
     */
    public static<T> Result ok(){
        return ok(null);
    }

    public static<T> Result ok(T data){
        return Result.build(data, ResultCodeEnum.SUCCESS);
    }

    /**
     * 返回结果为空，错误
     */
    public static<T> Result fail(){
        return fail(null);
    }

    public static<T> Result fail(T data){
        return Result.build(data, ResultCodeEnum.FAIL);
    }

    public static<T> Result fail(T data,Integer code,String message){
        return build(data,code,message);
    }

    public static<T> Result fail(String message){
        return build(null,201,message);
    }


    public static<T> Result fail(T data,ResultCodeEnum resultCodeEnum){
        return build(data,resultCodeEnum);
    }

}
