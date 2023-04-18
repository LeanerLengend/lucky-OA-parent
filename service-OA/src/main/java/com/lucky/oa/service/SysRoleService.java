package com.lucky.oa.service;

import com.github.pagehelper.PageInfo;
import com.lucky.model.system.SysRole;
import com.lucky.vo.system.SysRoleQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ChenYXCoding
 * @create 2023-03-06 20:36
 */

public interface SysRoleService  {

    // 查询系统中所有的角色信息
    List<SysRole> getAllSysRole();

    // 根据条件分页查询
    PageInfo<SysRole> page(SysRoleQueryVo sysRoleQueryVo, Integer page, Integer limit);

    // 添加系统角色
    Boolean save( SysRole sysRole);

    // 根据id查询系统角色
    SysRole getById(Integer id);

    // 根据id删除系统角色
    Boolean removeById(Integer id);

    // 根据id批量删除用户
    Boolean removeByIds( List<Integer> ids);

    // 更新用户
    Boolean updateById(SysRole sysRole);

}
