package com.lucky.oa.mapper;

import com.lucky.model.system.SysMenu;
import com.lucky.model.system.SysRoleMenu;
import com.lucky.vo.system.AssginMenuVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author ChenYXCoding
 * @create 2023-04-07 19:57
 */
@Mapper
public interface SysMenuMapper {

    // 根据parentId 查询到下面的子菜单
    List<SysMenu> getSysMenuListByParentId(Long parentId);

    // 查询菜单的所有数据
    List<SysMenu> list();

    // 根据id删除菜单
    Integer deleteMenuById(@Param("id")Long id);

    // 根据id查询菜单项，单个
    SysMenu getSysMenuById(@Param("id")Long id);

    Integer getChildCount(@Param("id") Long id);

    Integer insertMenu(SysMenu sysMenu);

    Integer updateMenu(SysMenu sysMenu);

    // 根据角色id来查询用户角色所属的订单
    List<SysRoleMenu> getMenuByRoleId(@Param("id")Long id);

    //为角色增加菜单
    Integer addMenuForRole(AssginMenuVo assginMenuVo);

    // 根据角色id移除所有的绑定的菜单
    Integer removeMenuByRoleId(@Param("id") Long id);


    // 通过用户id,查询用户可以操作的菜单有哪些
    List<SysMenu> getRouterMenuByUserId(Long id);

    // 通过用户id,查询用户可以操作的按钮有哪些,这里的按钮是一些string类型的，所以泛型是String类型
    List<String> getRouterMenuPermsByUserId(Long id);
}
