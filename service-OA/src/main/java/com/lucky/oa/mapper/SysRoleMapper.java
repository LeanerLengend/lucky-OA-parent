package com.lucky.oa.mapper;


import com.lucky.model.system.SysRole;
import com.lucky.vo.system.SysRoleQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMapper  {

    // 查询系统中所有的角色信息
    List<SysRole> getAllSysRole();

    // 根据条件分页查询
    List<SysRole> page(@Param("sysRoleQueryVo") SysRoleQueryVo sysRoleQueryVo);

    // 添加系统角色
    int save(SysRole sysRole);

    // 根据id查询系统角色
    SysRole getById(@Param("id")Integer id);

    // 根据id删除系统角色
    Integer removeById(@Param("id")Integer id);

    // 跟读id批量删除用户
     Integer removeByIds(@Param("list") List<Integer> ids);

     // 根据id修改用户
    Integer updateById(SysRole sysRole);

}
