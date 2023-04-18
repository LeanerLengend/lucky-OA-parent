package com.lucky.oa.service;
import com.lucky.model.system.SysMenu;
import com.lucky.vo.system.AssginMenuVo;
import com.lucky.vo.system.RouterVo;
import java.util.List;
import java.util.Map;

/**
 * @author ChenYXCoding
 * @create 2023-04-07 19:59
 */
public interface SysMenuService {

    // 根据parentId 查询到下面的子菜单
    List<SysMenu> getSysMenuListByParentId(Long parentId);

    // 查询菜单的所有数据
    List<SysMenu> list();

    // 获取菜单
    List<SysMenu> getMenu();

    // 根据id删除菜单
    Integer deleteMenuById(Long id);

    //查询子菜单的数量
    Integer getChildCount(Long id);

    // 插入菜单
    Integer insertMenu(SysMenu sysMenu);

    // 更新菜单
    Integer updateMenu(SysMenu sysMenu);

    // 根据角色id来查询用户角色所属的订单
    List<SysMenu> getMenuByRoleId(Long id);

    //为角色增加菜单
    Integer addMenuForRole(AssginMenuVo assginMenuVo);

    //根据角色id移除所有的菜单那现象
    Integer removeMenuByRoleId( Long id);

    // 通过用户id,查询用户可以操作的菜单有哪些
    List<RouterVo> getRouterMenuByUserId(Long id);

    // 通过用户id,查询用户可以操作的按钮有哪些,这里的按钮是一些string类型的，所以泛型是String类型
    List<String> getRouterMenuPermsByUserId(Long id);


}
