package com.lucky.oa.controller;


import com.github.pagehelper.PageInfo;
import com.lucky.common.result.Result;
import com.lucky.model.system.SysRole;
import com.lucky.oa.service.SysRoleService;
import com.lucky.vo.system.SysRoleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ChenYXCoding
 * @create 2023-03-07 18:56
 */
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/findAll")
    public Result findAll(){
        return Result.ok(sysRoleService.getAllSysRole());
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/findByValue/{page}/{limit}")
    public Result findAll(@PathVariable("page") Integer page,
                                         @PathVariable("limit") Integer limit,
                                         SysRoleQueryVo sysRoleQueryVo
    ){
        PageInfo<SysRole> pageInfo = sysRoleService.page(sysRoleQueryVo, page, limit);
        return Result.ok(pageInfo);
    }

//    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    // 请求参数
    @PostMapping("/addSysRole")
    public Result addSysRole(@RequestBody SysRole sysRole){
        return sysRoleService.save(sysRole) ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @GetMapping("/{id}")
    public Result querySysRoleById(@PathVariable("id") Integer id){
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @PutMapping("/{id}")
    public Result updateSysRoleById(@RequestBody SysRole sysRole){
        return sysRoleService.updateById(sysRole) ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @DeleteMapping("/{id}")
    public Result deleteSysRoleById(@PathVariable("id")Integer id){
        return sysRoleService.removeById(id) ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @DeleteMapping("/batchRemove")
    public Result deleteSysRoleByIds(@RequestBody List<Integer> id){
        return sysRoleService.removeByIds(id) ? Result.ok() : Result.fail();
    }




}
