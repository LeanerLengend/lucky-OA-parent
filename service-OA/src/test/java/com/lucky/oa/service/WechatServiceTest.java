package com.lucky.oa.service;

import com.lucky.vo.wechat.MenuVo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author lucky
 * @create 2023-05-17 21:03
 */
@SpringBootTest
public class WechatServiceTest {

    @Resource
    public WeChatMenuService weChatMenuService;

    @Test
    public void testBuidTree(){
        for (MenuVo menuVo : weChatMenuService.findMenuTree()) {
            System.out.println(menuVo);
        }
    }


}
