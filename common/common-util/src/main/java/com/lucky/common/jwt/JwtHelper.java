package com.lucky.common.jwt;

import io.jsonwebtoken.*;

import java.util.Date;

/**
 * @author lucky
 * @create 2023-04-11 19:29
 */
public class JwtHelper {
    // token过期时长
    private static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;

    // 编码的密钥
    private static String tokenSignKey = "123456";

    // 根据一个用户id,用户名称生成一个token
    public static String createToken(Long userId, String username) {
        String token = Jwts.builder()
                .setSubject("AUTH-USER") // 做一个分类
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)) // 设置有效时间
                // 设置主题内容
                .claim("userId", userId)
                .claim("username", username)
                // 设置签名部分
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                // 压缩字符串
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }



    // 生成token字符串，获取用户id
    public static Long getUserId(String token) {
        try {

            if(token.equals("") || token == null)return null;

            // 根据主体内容做一个内容解密
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            // 获取值
            Claims claims = claimsJws.getBody();
            Integer userId = (Integer) claims.get("userId");
            return userId.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 生成token字符串，获取用户userName
    public static String getUsername(String token) {
        try {
            if(token.equals("") || token == null)return null;
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("username");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String token = JwtHelper.createToken(4L, "lisi");
        System.out.println(token);
        System.out.println(JwtHelper.getUserId(token));
        System.out.println(JwtHelper.getUsername(token));
    }
}