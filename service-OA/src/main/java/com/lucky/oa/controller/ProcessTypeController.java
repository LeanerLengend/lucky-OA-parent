package com.lucky.oa.controller;

import com.github.pagehelper.PageInfo;
import com.lucky.common.result.Result;
import com.lucky.model.process.ProcessType;
import com.lucky.oa.service.ProcessTypeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lucky
 * @create 2023-05-07 16:34
 */
@RestController
@RequestMapping(value = "/admin/process/processType")
public class ProcessTypeController {

    @Resource
    private ProcessTypeService processTypeService;

    @GetMapping("/findAll")
    @PreAuthorize("hasAuthority('bnt.process.list')")
    public Result list(){
        return Result.ok(processTypeService.list());
    }


    @GetMapping("/{page}/{limit}")
    @PreAuthorize("hasAuthority('bnt.process.list')")
    public Result list(@PathVariable("page") Integer page,@PathVariable("limit") Integer  limit){
        PageInfo<ProcessType> list = processTypeService.list(page, limit);
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.process.list')")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable("id") Long id){
        return Result.ok(processTypeService.getProcessTypeById(id));
    }

    @PreAuthorize("hasAuthority('bnt.processType.remove')")
    @DeleteMapping("/remove/{id}")
    public Result del(@PathVariable("id") Long id){
        return processTypeService.delete(id) ? Result.ok() : Result.fail("删除失败！");
    }

    @PreAuthorize("hasAuthority('bnt.processType.update')")
    @PutMapping("/update")
    public Result update(@RequestBody ProcessType processType){
        Boolean update = processTypeService.update(processType);
        return update ? Result.ok() : Result.fail("更新失败");
    }

    @PreAuthorize("hasAuthority('bnt.processType.add')")
    @PostMapping("/save")
    public Result add(@RequestBody ProcessType processType){
        Boolean update = processTypeService.add(processType);
        return update ? Result.ok() : Result.fail("添加失败！");
    }

}
