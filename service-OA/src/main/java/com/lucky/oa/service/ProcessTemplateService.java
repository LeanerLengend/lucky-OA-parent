package com.lucky.oa.service;

import com.github.pagehelper.PageInfo;
import com.lucky.model.process.ProcessTemplate;
import com.lucky.model.process.ProcessType;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lucky
 * @create 2023-05-08 8:48
 */
public interface ProcessTemplateService {

    // 分页查询
    PageInfo<ProcessTemplate> list(Integer page, Integer limit);

    // 根据id查询审批类型
    ProcessTemplate getProcessTemplateById(Long id);

    // 增删改查
    Boolean add(ProcessTemplate processTemplate);

    Boolean delete(Long id);

    Boolean update(ProcessTemplate processTemplate);

    Boolean uploadProcessTemplate(MultipartFile multipartFile);

    /**
     * 发布模板，其中模板中包含activity流程文件，需要部署
     * 相关部署在ProcessService当中
     * @param id
     * @return
     */
    Boolean publish(Long id);


}
