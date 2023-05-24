package com.lucky.oa.controller;

import com.github.pagehelper.PageInfo;
import com.lucky.common.result.Result;
import com.lucky.model.process.ProcessType;
import com.lucky.oa.service.ProcessService;
import com.lucky.oa.service.ProcessTemplateService;
import com.lucky.oa.service.ProcessTypeService;
import com.lucky.vo.process.ApprovalVo;
import com.lucky.vo.process.ProcessFormVo;
import com.lucky.vo.process.ProcessQueryVo;
import com.lucky.vo.process.ProcessVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lucky
 * @create 2023-05-09 19:35
 */
@RestController
@RequestMapping(value = "/admin/process")
@CrossOrigin
public class ProcessController {

    @Resource
    private ProcessService processService;

    @Resource
    private ProcessTypeService processTypeService;

    @Resource
    private ProcessTemplateService processTemplateService;


    /**
     * 查询用户发起的任务
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(@PathVariable Long page,@PathVariable Long limit) {
        return Result.ok(processService.findStarted(page,limit));
    }


    /**
     * 查询已经处理过已经处理的任务
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(@PathVariable Long page,@PathVariable Long limit) {
        return Result.ok(processService.findProcessed(page,limit));
    }

    @PostMapping("/approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        if (approvalVo.getStatus() == 1L) {
            processService.approve(approvalVo);
        } else {
            processService.reject(approvalVo);
        }
        return Result.ok();
    }

    // 查看审批详情信息
    @GetMapping("/show/{id}")
    public Result show(@PathVariable Long id) {
        return Result.ok(processService.show(id));
    }

    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(@PathVariable Long page, @PathVariable Long limit) {
        return Result.ok(processService.queryBackLogTask(page,limit));
    }

    /**
     * 启动流程
     * @param processFormVo
     * @return
     */
    @PostMapping("/startUp")
    public Result start(@RequestBody ProcessFormVo processFormVo) {
        processService.startProcess(processFormVo);
        return Result.ok();
    }


    /**
     * 微信公众号请求
     * @return
     */
    @GetMapping("/findProcessType")
    public Result finAllProcessType(){
        List<ProcessType> allProcessTypeAndTemplate = processService.getAllProcessTypeAndTemplate();
        return Result.ok(allProcessTypeAndTemplate);
    }

    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result getProcessTemplate(@PathVariable Long processTemplateId ){
        return Result.ok(processTemplateService.getProcessTemplateById(processTemplateId));
    }


    @PreAuthorize("hasAuthority('bnt.process.list')")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        ProcessQueryVo processQueryVo) {
        PageInfo<ProcessVo> list = processService.list(processQueryVo, page.intValue(), limit.intValue());
        return Result.ok(list);
    }


}
