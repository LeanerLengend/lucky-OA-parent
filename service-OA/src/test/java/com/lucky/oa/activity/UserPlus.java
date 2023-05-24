package com.lucky.oa.activity;
import org.springframework.stereotype.Component;
/**
 * @author lucky
 * @create 2023-04-27 10:39
 */
@Component
public class UserPlus {

    public String getUsernameById(int id){
        if(id == 1){
            return "zhangsan";
        }else {
            return "lisi";
        }
    }

}