package com.lucky.oa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ChenYXCoding
 * @create 2023-03-06 11:24
 */
// 设定组件扫描的路径
@ComponentScan("com.lucky")
@SpringBootApplication
public class LuckyOAApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyOAApplication.class, args);
    }

}
