package com.lucky.springsecurity.custom;

import com.lucky.model.system.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;
import java.util.Collections;

/**
 * @author lucky
 * @create 2023-04-15 11:08
 */
public class CustomUser extends User {

    private SysUser sysUser;

    public CustomUser() {
        super(null,null, Collections.emptyList());
    }


    public CustomUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        super(sysUser.getUsername(), sysUser.getPassword(), authorities);
        this.sysUser = sysUser;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

}
