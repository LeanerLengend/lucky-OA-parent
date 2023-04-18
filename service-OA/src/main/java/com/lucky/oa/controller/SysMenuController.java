package com.lucky.oa.controller;

import com.lucky.common.result.Result;
import com.lucky.model.system.SysMenu;
import com.lucky.oa.service.SysMenuService;
import com.lucky.vo.system.AssginMenuVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ChenYXCoding
 * @create 2023-04-07 20:00
 */
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @GetMapping("/findNodes")
    public Result findNodes() {
        List<SysMenu> list = sysMenuService.getMenu();
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.remove')")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysMenuService.deleteMenuById(id);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.add')")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu permission) {
        sysMenuService.insertMenu(permission);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysMenu permission) {
        sysMenuService.updateMenu(permission);
        return Result.ok();
    }

    /**
     * 根据用户获取角色菜单
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.getMenuByRoleId(roleId);
        return Result.ok(list);
    }

    /**
     * 为角色分配菜单
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuVo assignMenuVo) {
        sysMenuService.addMenuForRole(assignMenuVo);
        return Result.ok();
    }

}
