package com.lucky.oa.service.impl;

import com.lucky.model.system.SysUser;
import com.lucky.oa.mapper.SysUserRoleMapper;
import com.lucky.oa.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
/**
 * @author ChenYXCoding
 * @create 2023-04-03 20:18
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysUser getRolesByUserId(Long id) {
        return sysUserRoleMapper.getRolesByUserId(id);
    }

    @Override
    public Integer assignRoles(Long userId, List<Long> ids) {
        return sysUserRoleMapper.assignRoles(userId,ids);
    }

    @Override
    public Integer removeRolesForUserById(Long id) {
        return sysUserRoleMapper.removeRolesForUserById(id);
    }
}
