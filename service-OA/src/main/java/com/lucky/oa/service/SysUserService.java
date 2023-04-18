package com.lucky.oa.service;


import com.github.pagehelper.PageInfo;
import com.lucky.model.system.SysRole;
import com.lucky.model.system.SysUser;
import com.lucky.vo.system.LoginVo;
import com.lucky.vo.system.SysRoleQueryVo;
import com.lucky.vo.system.SysUserQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ChenYXCoding
 * @create 2023-03-16 20:08
 */
public interface SysUserService  {
    // 根据条件分页查询
    PageInfo<SysUser> page(SysUserQueryVo sysUserQueryVo,Integer page,Integer limit);

    // 添加系统用户
    Boolean save( SysUser SysUser);

    // 根据id查询系统用户
    SysUser getById(Long id);

    // 根据id删除系统用户
    Boolean removeById(Integer id);

    // 根据id批量删除用户
    Boolean removeByIds( List<Integer> ids);

    // 更新用户
    Boolean updateById(SysUser sysUser);

    // 根据用户id查询用户的所有角色信息
    Map<String,Object> getRolesByUserId(Long id);

    // 为用户分配角色
   Boolean assignRoles(Long userId,List<Long> roleIds);

    // 启用或者禁用用户
    int enableOrDisable(Long userId, Integer status);

    // 根据用户名密码查询数据库中是否存在用户，返回的token字符串
    String  login(LoginVo loginVo);

    // 根据用户名查询是否有当前用户
    Integer checkUsername(String username);

    Map<String ,Object> info(String token);

    // 根据用户名查询用户
    SysUser getUserEntityByUserName(String username);



}
