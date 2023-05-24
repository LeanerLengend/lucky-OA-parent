package com.lucky.oa.service;

import com.github.pagehelper.PageInfo;
import com.lucky.model.wechat.Menu;
import com.lucky.vo.wechat.MenuVo;

import java.util.List;

/**
 * @author lucky
 * @create 2023-05-17 18:14
 */
public interface WeChatMenuService {

    /**
     * 将菜单封装成微信公众号可以识别到的形式
     */
    void syncMenu();

    List<MenuVo> findMenuTree();

    PageInfo<MenuVo> page(Long page, Long limit);

    // 根据id删除菜单
    Integer deleteMenuById(Long id);

    // 根据id查询菜单项，单个
    Menu getMenuById(Long id);

    Integer insertMenu(Menu menu);

    Integer updateMenu(Menu menu);

    void removeMenu();
}
