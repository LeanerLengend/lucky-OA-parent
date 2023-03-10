package com.lucky.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Function;
import com.lucky.common.Result;
import com.lucky.model.system.SysRole;
import com.lucky.oa.service.SysRoleService;
import com.lucky.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChenYXCoding
 * @create 2023-03-07 18:56
 */
@Api("系统用户管理相关操作")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation("查询所有的系统角色")
    @GetMapping("/findAll")
    public Result findAll(){
        return Result.ok(sysRoleService.list());
    }

    @ApiOperation("根据条件，分页插件查询")
    @GetMapping("/{page}/{limit}")
    public Result findAll(@PathVariable("page") Integer page,
                                         @PathVariable("limit") Integer limit,
                                         SysRoleQueryVo sysRoleQueryVo
    ){
        // 设置分页
        Page<SysRole> sysRolePage = new Page<>(page,limit);

        String roleName = sysRoleQueryVo.getRoleName();
        // 作用于什么对象的条件
        LambdaQueryWrapper<SysRole> queryWrapper = null;
        // 封装条件，条件不为空封装
        if(!StringUtils.isEmpty(roleName)){
            queryWrapper = new LambdaQueryWrapper<>();
            Function<SysRole, String> getRoleName = SysRole::getRoleName;
            queryWrapper.like(SysRole::getRoleName,roleName);
        }
        // 返回分页结果
        IPage<SysRole> pageModel =  sysRoleService.page(sysRolePage, queryWrapper);
        return Result.ok(pageModel);
    }


    // 请求参数
    @ApiOperation("添加系统用户")
    @PostMapping("/addSysRole")
    public Result addSysRole(SysRole sysRole){
        return sysRoleService.save(sysRole) ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据id查询")
    @GetMapping("/{id}")
    public Result querySysRoleById(@PathVariable("id") Integer id){
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }


    @ApiOperation("修改用户")
    @PutMapping("/{id}")
    public Result updateSysRoleById(SysRole sysRole){
        return sysRoleService.updateById(sysRole)? Result.ok() : Result.fail();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Result deleteSysRoleById(@PathVariable("id")Integer id){
        return sysRoleService.removeById(id) ? Result.ok() : Result.fail();
    }

    @ApiOperation("批量删除用户")
    @DeleteMapping("/batchRemove")
    public Result deleteSysRoleByIds(@RequestBody List<Integer> id){
        return sysRoleService.removeByIds(id) ? Result.ok() : Result.fail();
    }

}
