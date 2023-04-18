package com.lucky.oa.service.impl;

import com.lucky.common.exception.UserOperateException;
import com.lucky.model.system.SysMenu;
import com.lucky.model.system.SysRoleMenu;
import com.lucky.oa.mapper.SysMenuMapper;
import com.lucky.oa.service.SysMenuService;
import com.lucky.vo.system.AssginMenuVo;
import com.lucky.vo.system.MetaVo;
import com.lucky.vo.system.RouterVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenYXCoding
 * @create 2023-04-07 19:59
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> getSysMenuListByParentId(Long parentId) {
        return sysMenuMapper.getSysMenuListByParentId(parentId);
    }

    @Override
    public List<SysMenu> list() {
        return sysMenuMapper.list();
    }

    @Override
    public List<SysMenu> getMenu() {
        // 获取菜单栏的所有数据
        List<SysMenu> list = sysMenuMapper.list();
        List<SysMenu> sysMenus = this.buildTree(list);
        return sysMenus;
    }

    /**
     * 查询该菜单是否有子菜单，如果有子菜单，就不能删除
     * @param id
     * @return
     */
    @Override
    public Integer deleteMenuById(Long id) {
        if(this.getChildCount(id) > 0){
            throw new UserOperateException("该菜单包含多个子菜单！,请先删除子菜单！");
        }
        return sysMenuMapper.deleteMenuById(id);
    }

    @Override
    public Integer getChildCount(Long id) {
        return sysMenuMapper.getChildCount(id);
    }

    @Override
    public Integer insertMenu(SysMenu sysMenu) {
        return sysMenuMapper.insertMenu(sysMenu);
    }

    @Override
    public Integer updateMenu(SysMenu sysMenu) {
        if(sysMenu != null && sysMenu.getParentId() != null){
            throw new UserOperateException("输入数据有误！,缺少id！");
        }
        return sysMenuMapper.updateMenu(sysMenu);
    }


    /**
     * 这个课程教授的竟然是返回一个角色树，我就不理解，明明可以返回可以分配的
     * @param id
     * @return
     */
    @Override
    public List<SysMenu> getMenuByRoleId(Long id) {
        List<SysRoleMenu> roleMenu = sysMenuMapper.getMenuByRoleId(id);
        List<SysMenu> allMenu = sysMenuMapper.list();
        for(SysRoleMenu srm : roleMenu){
            for(SysMenu item : allMenu){
                if(item.getStatus() != 1){
                    allMenu.remove(item);
                }
                if(item.getId().longValue() == srm.getMenuId()){
                    // 通过这个的值，标明这个东西是不是选中
                    item.setSelect(true);
                }
            }
        }
        return this.buildTree(allMenu);
    }

    @Override
    public Integer addMenuForRole( AssginMenuVo assginMenuVo) {
        if(assginMenuVo.getRoleId() == 0 || assginMenuVo.getMenuIdList().size() == 0){
            throw  new UserOperateException("菜单列表为空");
        }else {
            // 删除角色之间的菜单
            this.removeMenuByRoleId(assginMenuVo.getRoleId());
            return sysMenuMapper.addMenuForRole(assginMenuVo);
        }
    }


    @Override
    public Integer removeMenuByRoleId(Long id) {
        return id != 0 ? sysMenuMapper.removeMenuByRoleId(id) : 0;
    }

    @Override
    public List<RouterVo> getRouterMenuByUserId(Long id) {
        List<SysMenu> userMenu = sysMenuMapper.getRouterMenuByUserId(id);
        List<SysMenu> sysMenusTree = this.buildTree(userMenu);
        List<RouterVo> routerVos = this.buildRouter(sysMenusTree);
        return routerVos;
    }

    /**
     * 构建路由结构，List<SysMenu>存储的是一个已经构建成功的树形结构菜单，但是并不是真正vue前端要用到的路由菜单，因此需要在这里使用
     * @param sysMenusTree
     * @return
     */
    public List<RouterVo> buildRouter(List<SysMenu> sysMenusTree){
        // 路由格式为[{},{}]
        List<RouterVo> listToUse = new ArrayList<>();
        for(SysMenu item: sysMenusTree){
            RouterVo router = new RouterVo();
            // 初始化基础信息
            this.initRouterVo(router,item,false);
            List<SysMenu> children = item.getChildren();
            // 如果类型是1，那么子菜单应该是一个按钮，如果是一个按钮，但是还有路由地址，
            // 那么就应该是一个隐藏路由，需要给他初始化为隐藏路由
            if(item.getType().intValue() == 1){
                // 看一看本来应该是按钮节点的是不是隐藏节点
                for (SysMenu child : children) {
                    if(child.getComponent() != null && !child.getComponent().equals("")){
                        RouterVo hiddenRouterVo = new RouterVo();
                        this.initRouterVo(hiddenRouterVo,child,true);
                        listToUse.add(hiddenRouterVo);
                    }
                }
            } else {
                // 查询出来的数sysMenusTree应该是用户要显示出来的树，那么需要可以显示
                if (!CollectionUtils.isEmpty(children)) {
                    if(children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    // 设置子路由
                    router.setChildren(buildRouter(children));
                }
            }
            listToUse.add(router);
        }
        return listToUse;
    }

    /**
     * 初始化路由信息
     * @param router
     * @param sysMenu
     * @return
     */
    private void initRouterVo(RouterVo router,SysMenu sysMenu,Boolean isHidden){
        router.setHidden(isHidden);
        router.setAlwaysShow(false);
        router.setPath(getRouterPath(sysMenu));
        router.setComponent(sysMenu.getComponent());
        // 角标信息
        router.setMeta(new MetaVo(sysMenu.getName(), sysMenu.getIcon()));
    }


    /**
     * 获取路由地址
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }


    @Override
    public List<String> getRouterMenuPermsByUserId(Long id) {
        List<String> perms = sysMenuMapper.getRouterMenuPermsByUserId(id);
        return perms;
    }

    /**
     * @param list
     * @return 可能返回值有多个菜单数，因此这里返回了一个list，而不是一个sysmenu,
     */
    public List<SysMenu> buildTree(List<SysMenu> list){
        List<SysMenu> listToUse = new ArrayList<>();
        for(SysMenu sysMenu : list){
            // 遍历找到根节点
            if( sysMenu.getParentId() == 0L){
                // 这就是代表了得到了根节点，树根,然后从这里来遍历
                listToUse.add(findNodes(sysMenu,list));
            }
        }
        return listToUse;
    }

    /**
     * @param sysMenu 入口，根
     * @param allMenu 所有菜单栏中的数据,也就是说相当于数据库中的所有数据
     * @return
     */
    public SysMenu findNodes(SysMenu sysMenu,List<SysMenu> allMenu){
        Long parentId = sysMenu.getId();
        // 这里是为了初始化当前子菜单，因为父菜单的childList默认是null，如果操作了有可能会空指针，所以需要初始化一下
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for(SysMenu item : allMenu){
            if(item.getParentId() == parentId){
                sysMenu.getChildren().add(item);
                // 查找子节点的所有数据
                findNodes(item,allMenu);
            }
        }
        return sysMenu;
    }



    /**
     * 以下是轻松的java代码，累死mysql的代码，可用，不可取
     * @param sysMenuList 系统管理下的几个顶级菜单
     * @return 返回这个几个顶级菜单，内部包装好了子菜单
    public List<SysMenu> buildTree(List<SysMenu> sysMenuList){
       for(SysMenu sysMenu : sysMenuList){
            findNodes(sysMenu);
       }
       return sysMenuList;
    }

    public SysMenu findNodes(SysMenu sysMenu){
        Long id = sysMenu.getId();
        List<SysMenu> childMenuList = getSysMenuListByParentId(id);
        sysMenu.setChildren(childMenuList);
        for (SysMenu child : childMenuList){
            SysMenu nodes = findNodes(child);
        }
        return sysMenu;
    }

    */

}
