package com.lucky.oa.controller;

import com.lucky.common.result.Result;
import com.lucky.model.wechat.Menu;
import com.lucky.oa.service.WeChatMenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lucky
 * @create 2023-05-17 18:23
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/wechat/menu")
public class WeChatMenuController {

    @Resource
    private WeChatMenuService weChatMenuService;


    @PreAuthorize("hasAuthority('bnt.menu.removeMenu')")
    @DeleteMapping("/removeMenu")
    public Result removeMenu() {
        weChatMenuService.removeMenu();
        return Result.ok();
    }

    /**
     * 即发送到微信方，使得所有用户看到的菜单界面和管理员想要的相同
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.menu.syncMenu')")
    @GetMapping("/syncMenu")
    public Result createMenu() {
        weChatMenuService.syncMenu();
        return Result.ok();
    }


    @PreAuthorize("hasAuthority('bnt.menu.list')")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        return Result.ok(weChatMenuService.getMenuById(id));
    }

    @PreAuthorize("hasAuthority('bnt.menu.add')")
    @PostMapping("/save")
    public Result save(@RequestBody Menu menu) {
        return Result.ok(weChatMenuService.insertMenu(menu));
    }

    @PreAuthorize("hasAuthority('bnt.menu.update')")
    @PutMapping("/update")
    public Result updateById(@RequestBody Menu menu) {
        return Result.ok(weChatMenuService.updateMenu(menu));
    }

    @PreAuthorize("hasAuthority('bnt.menu.remove')")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        return Result.ok(weChatMenuService.deleteMenuById(id));
    }

    @PreAuthorize("hasAuthority('bnt.menu.list')")
    @GetMapping("/findMenuInfo")
    public Result findMenuInfo() {
        return Result.ok(weChatMenuService.findMenuTree());
    }

}
