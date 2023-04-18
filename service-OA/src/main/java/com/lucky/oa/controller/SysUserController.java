package com.lucky.oa.controller;

import com.github.pagehelper.PageInfo;
import com.lucky.common.result.Result;
import com.lucky.model.system.SysUser;
import com.lucky.oa.service.SysUserService;
import com.lucky.vo.system.AssginRoleVo;
import com.lucky.vo.system.SysUserQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author ChenYXCoding
 * @create 2023-03-16 20:16
 */
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("/querySysUser/{page}/{limit}")
    public Result querySysUser(
            @PathVariable("page")Integer page,
            @PathVariable("limit")Integer limit,
            SysUserQueryVo sysUserQueryVo
    ){
        PageInfo<SysUser> pageInfo = sysUserService.page(sysUserQueryVo,page,limit);
        return Result.ok(pageInfo);
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("/{sysUserId}")
    public Result querySysUser(@PathVariable Long sysUserId){
        return Result.ok(sysUserService.getById(sysUserId));
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    @PostMapping("/saveSysUser")
    public Result saveSysUser(@RequestBody SysUser sysUser){
         return sysUserService.save(sysUser) ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @PutMapping("/updateSysUser")
    public Result updateSysUser(@RequestBody SysUser sysUser){
        return sysUserService.updateById(sysUser) ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @DeleteMapping("/{sysUserId}")
    public Result singleDeleteSysUser(@PathVariable("sysUserId") Integer sysUserId){
        return sysUserService.removeById(sysUserId) ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @DeleteMapping("/batchesDeleteSysUser")
    public Result singleDeleteSysUser(@RequestBody List<Integer> sysUserIds){
        return sysUserService.removeByIds(sysUserIds) ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @GetMapping("/toAssign/{userId}")
    public Result getAssign(@PathVariable("userId") Long id){
        Map<String, Object> map = sysUserService.getRolesByUserId(id);
        return Result.ok(map);
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        return  sysUserService.assignRoles(assginRoleVo.getUserId(), assginRoleVo.getRoleIdList()) ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result enableOrDisabled(@PathVariable("id") Long id,@PathVariable("status") Integer status){
        sysUserService.enableOrDisable(id,status);
        return  Result.ok();
    }
}
