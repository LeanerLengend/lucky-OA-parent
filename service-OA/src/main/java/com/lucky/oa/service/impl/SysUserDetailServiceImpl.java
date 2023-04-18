package com.lucky.oa.service.impl;
import com.lucky.common.exception.UserOperateException;
import com.lucky.model.system.SysUser;
import com.lucky.oa.service.SysMenuService;
import com.lucky.oa.service.SysUserService;
import com.lucky.springsecurity.custom.CustomUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 可以直接继承框架提供的UserDetailsService类，也可以直接像下面这样直接实现也可以
 * @author lucky
 * @create 2023-04-15 11:19
 */
@Service
public class SysUserDetailServiceImpl implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    /**
     * @param s 用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserEntityByUserName(s);

        if(sysUser == null){
            throw  new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus() != 0){
            throw  new UserOperateException("账号已停用！");
        }

        // string权限
        List<String> perms = sysMenuService.getRouterMenuPermsByUserId(sysUser.getId());

        // 要求的是GrantedAuthority类，GrantedAuthority是个接口，底下有实现类SimpleGrantedAuthority,用这个即可
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(String str : perms){
            authorities.add(new SimpleGrantedAuthority(str));
        }


        return new CustomUser(sysUser, authorities);
    }
}
