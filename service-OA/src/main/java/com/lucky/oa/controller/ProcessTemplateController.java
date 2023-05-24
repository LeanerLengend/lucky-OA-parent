package com.lucky.oa.controller;
import com.github.pagehelper.PageInfo;
import com.lucky.common.result.Result;
import com.lucky.model.process.ProcessTemplate;
import com.lucky.oa.service.ProcessTemplateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lucky
 * @create 2023-05-08 9:01
 */
@RestController
@RequestMapping("/admin/process/processTemplate")
public class ProcessTemplateController {

    @Resource
    private ProcessTemplateService processTemplateService;

    @PreAuthorize("hasAuthority('bnt.processTemplate.list')")
    @GetMapping("/{page}/{limit}")
    public Result index(
            @PathVariable Long page,
            @PathVariable Long limit) {
        PageInfo<ProcessTemplate> list = processTemplateService.list(page.intValue(), limit.intValue());
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.list')")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessTemplate processTemplate = processTemplateService.getProcessTemplateById(id);
        return Result.ok(processTemplate);
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @PostMapping("/save")
    public Result save(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.add(processTemplate);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @PutMapping("/update")
    public Result updateById(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.update(processTemplate);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.remove')")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        processTemplateService.delete(id);
        return Result.ok();
    }


    @PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) {
        Boolean flag = processTemplateService.uploadProcessTemplate(file);
        if(flag){
            Map<String, Object> map = new HashMap<>();
            String fileName = file.getOriginalFilename();
            //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
            map.put("processDefinitionPath", "processes/" + fileName);
            map.put("processDefinitionKey", fileName.substring(0, fileName.lastIndexOf(".")));
            return Result.ok(map);
        }else {
            return Result.fail("上传文件失败！，请联系管理员！");
        }
    }

    @PreAuthorize("hasAuthority('bnt.processTemplate.publish')")
    @GetMapping("/publish/{id}")
    public Result publish(@PathVariable Long id) {

         return  processTemplateService.publish(id) ? Result.ok() : Result.fail();
    }
}
