package com.lucky.springsecurity.custom;

import com.lucky.common.utils.MD5Helper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author lucky
 * @create 2023-04-15 10:54
 */
@Component
public class CustomMd5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return MD5Helper.encrypt(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        // 判断两个密码是否相同
        return s.equals(encode(charSequence));
    }
}
