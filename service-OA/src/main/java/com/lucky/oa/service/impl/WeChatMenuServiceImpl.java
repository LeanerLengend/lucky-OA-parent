package com.lucky.oa.service.impl;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.model.wechat.Menu;
import com.lucky.oa.mapper.WeChatMenuMapper;
import com.lucky.oa.service.WeChatMenuService;
import com.lucky.vo.wechat.MenuVo;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lucky
 * @create 2023-05-17 18:15
 */
@Service
public class WeChatMenuServiceImpl implements WeChatMenuService {

    @Resource
    private WeChatMenuMapper weChatMenuMapper;

    @Resource
    private WxMpService wxMpService;

    @Override
    public void removeMenu() {
        try {
            // 清除菜单
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void syncMenu() {
        // 查询所有所有的微信菜单列表
        List<MenuVo> menuTree = this.findMenuTree();
        // 封装成微信想要的格式
        JSONArray firstOrderList = new JSONArray();
        for(MenuVo menuVo : menuTree){
            // 第一级菜单
            JSONObject firstOrder = new JSONObject();
            firstOrder.put("name",menuVo.getName());
            // 如果该菜单没有子菜单，则代表这是仅仅这个只有子菜单
            if(CollectionUtil.isEmpty(menuVo.getChildren())){
                firstOrder.put("type",menuVo.getType());
                // change
                firstOrder.put("url","http://1ao5msve.shenzhuo.vip:50998/#"+menuVo.getUrl());
            }else {
                // 有子菜单，遍历封装
                JSONArray childrenButtonList = new JSONArray();
                for (MenuVo child : menuVo.getChildren()) {
                    // 第二级菜单
                    JSONObject secondOrder = new JSONObject();
                    secondOrder.put("name",child.getName());
                    secondOrder.put("type",child.getType());
                    if(child.getType().equals("view")){
                        secondOrder.put("url","http://1ao5msve.shenzhuo.vip:50998/#"+child.getUrl());
                    }else {
                        secondOrder.put("key", child.getMeunKey());
                    }
                    childrenButtonList.add(secondOrder);
                }
                firstOrder.put("sub_button",childrenButtonList);
            }
            firstOrderList.add(firstOrder);
        }
        JSONObject menu = new JSONObject();
        menu.put("button",firstOrderList);
        try {
            // 用工具类进行实现
            wxMpService.getMenuService().menuCreate(menu.toJSONString());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MenuVo> findMenuTree() {
        // 获取所有菜单数据
        List<MenuVo> menuList = weChatMenuMapper.list();
        // 根据菜单数据构建成树形结构
        // 找到入口,单个树，因此就这样了
        List<MenuVo> menuListToUse = new ArrayList<>();
        for (MenuVo menuVo : menuList) {
            if(menuVo.getParentId() == 0L){
                MenuVo menuVo1 = buildTree(menuVo);
                menuListToUse.add(menuVo1);
            }
        }
        return menuListToUse;
    }

    /**
     * 构建树
     * @param menu
     * @return
     */
    public MenuVo buildTree(MenuVo menu) {
        // 查询该菜单下面是否有子菜单
        Long id = menu.getId();
        List<MenuVo> menuList = weChatMenuMapper.getChildNode(id);
        for(MenuVo item : menuList){
            this.buildTree(item);
        }
        menu.setChildren(menuList);
        return menu;
    }



    @Override
    public PageInfo<MenuVo> page(Long page, Long limit) {
        PageHelper.startPage(page.intValue(),limit.intValue());
        List<MenuVo> list = weChatMenuMapper.list();
        return new PageInfo<MenuVo>(list);
    }

    @Override
    public Integer deleteMenuById(Long id) {
        return weChatMenuMapper.deleteMenuById(id);
    }

    @Override
    public Menu getMenuById(Long id) {
        return weChatMenuMapper.getMenuById(id);
    }

    @Override
    public Integer insertMenu(Menu menu) {
        return weChatMenuMapper.insertMenu(menu);
    }

    @Override
    public Integer updateMenu(Menu menu) {
        return weChatMenuMapper.updateMenu(menu);
    }


}
