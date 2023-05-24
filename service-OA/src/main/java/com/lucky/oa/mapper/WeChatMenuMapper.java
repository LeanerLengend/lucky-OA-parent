package com.lucky.oa.mapper;

import com.lucky.model.system.SysMenu;
import com.lucky.model.wechat.Menu;
import com.lucky.vo.wechat.MenuVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lucky
 * @create 2023-05-17 17:50
 */
@Mapper
public interface WeChatMenuMapper {

    // 根据parent_id 查询子节点
    List<MenuVo> getChildNode(@Param("id")Long id);

    List<MenuVo> list();

    // 根据id删除菜单
    Integer deleteMenuById(@Param("id")Long id);

    // 根据id查询菜单项，单个
    Menu getMenuById(@Param("id")Long id);

    Integer insertMenu(Menu menu);

    Integer updateMenu(Menu menu);


}
