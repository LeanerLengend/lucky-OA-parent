package com.lucky.oa.wechat.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 绑定配置文件
 * @author lucky
 * @create 2023-05-19 9:34
 */
@Data
@ConfigurationProperties(prefix = "wechat")
@Component
public class WechatAccountConfig {

    private String mpAppId;
    private String  mpAppSecret;

}
