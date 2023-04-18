package com.lucky.oa.mapper;

import com.lucky.model.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ChenYXCoding
 * @create 2023-04-03 19:36
 */
@Mapper
public interface SysUserRoleMapper {

    // 根据用户id 查询出用户和用户的角色
    // 包含角色
    SysUser getRolesByUserId(@Param("id")Long id);

    // 为用户分配角色
    Integer assignRoles(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    //移除用户的所有角色
    Integer removeRolesForUserById(@Param("id") Long id);


}
