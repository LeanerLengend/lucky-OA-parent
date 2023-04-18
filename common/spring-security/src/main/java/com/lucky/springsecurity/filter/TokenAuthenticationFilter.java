package com.lucky.springsecurity.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lucky.common.jwt.JwtHelper;
import com.lucky.common.result.ResponseUtil;
import com.lucky.common.result.Result;
import com.lucky.common.result.ResultCodeEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author lucky
 * @create 2023-04-17 10:44
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

        logger.info("uri:"+request.getRequestURI());

        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        // 获取到用户的认证，把他放到路径下,其他请求请求的时候是否有登录信息，如果没有登录信息
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            // 没有token的情况下，返回信息
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
            if (!StringUtils.isEmpty(username)) {

                // 从Redis里面取到授权信息,当时放的json数据，所以在这里要转化一下
                String o = (String)redisTemplate.opsForValue().get(username);
                List<SimpleGrantedAuthority> authorities = null;
                if(!StringUtils.isEmpty(o)){
                    authorities = JSON.parseArray(o,SimpleGrantedAuthority.class);
                }
                // 登录后会想token中放id，和用户名，没有密码！，所以这里的credentials为null
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }
        }
        return null;
    }
}
