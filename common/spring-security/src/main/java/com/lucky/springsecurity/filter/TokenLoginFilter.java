package com.lucky.springsecurity.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucky.common.jwt.JwtHelper;
import com.lucky.common.result.ResponseUtil;
import com.lucky.common.result.Result;
import com.lucky.springsecurity.custom.CustomUser;
import com.lucky.vo.system.LoginVo;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义登录过滤器
 * @author lucky
 * @create 2023-04-15 16:35
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 初始化一下该过滤器的属性，该属性是由spring配置类来调用的
     *  .addFilter(new TokenLoginFilter(authenticationManager(),xxxx));
     * @param authenticationManager
     */
    public TokenLoginFilter(AuthenticationManager authenticationManager,RedisTemplate redisTemplate) {
        this.setAuthenticationManager(authenticationManager);
        //指定登录接口及提交方式，可以指定任意路径
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login","POST"));
        this.redisTemplate = redisTemplate;
    }

    /**
     *在用户提交认证请求时进行身份验证。如果用户名和密码正确，那么认证过程就会继续，否则会返回一个登录失败的页面。
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 通过jckson解析，解析到用户提交的信息是一个表单LoginVo这个表单
        LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
        //new UsernamePasswordAuthenticationToken(账号，密码)
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
        // 认证管理器，认证用户
        return this.getAuthenticationManager().authenticate(authentication);
    }

    /**
     * 登录成功后执行的操作
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        // 得到当前用户的对象
        CustomUser principal = (CustomUser)authResult.getPrincipal();

        // 获取当前用户对象的权限有哪一些
        Collection<GrantedAuthority> authorities = principal.getAuthorities();

        String username = principal.getUsername();
        // 把数据放到redis中 key为用户名，value为authorities
        // redis不能放value不能放集合，所以把他转化成json格式
        redisTemplate.opsForValue().set(username, JSON.toJSONString(authorities));

        // 根据当前用户生成token
        String token = JwtHelper.createToken(principal.getSysUser().getId(), username);
        // 返回，和前端对接
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        // 自定义工具类返回
        ResponseUtil.out(response, Result.ok(map));
    }


    /**
     * 登录失败返回信息
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        // 这里写的比较简单，就是输出一下就OK了
        String message = failed.getCause().getMessage();
        ResponseUtil.out(response, Result.fail(message));
    }
}
