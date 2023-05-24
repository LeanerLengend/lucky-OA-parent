package com.lucky.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.common.exception.UserOperateException;
import com.lucky.model.process.ProcessTemplate;
import com.lucky.oa.mapper.ProcessTemplateMapper;
import com.lucky.oa.service.ProcessService;
import com.lucky.oa.service.ProcessTemplateService;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * @author lucky
 * @create 2023-05-08 8:49
 */
@Service
public class ProcessTemplateServiceImpl implements ProcessTemplateService {

    @Resource
    private ProcessTemplateMapper processTemplateMapper;

    @Resource
    private ProcessService processService;

    @Resource
    private RepositoryService repositoryService;


    @Override
    public PageInfo<ProcessTemplate> list(Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        return  new PageInfo<ProcessTemplate>(processTemplateMapper.list());
    }

    @Override
    public ProcessTemplate getProcessTemplateById(Long id) {
        return processTemplateMapper.getProcessTemplateById(id);
    }

    @Override
    public Boolean add(ProcessTemplate processTemplate) {
        return  processTemplateMapper.add(processTemplate) > 0? true : false;
    }

    @Override
    public Boolean delete(Long id) {
        return  processTemplateMapper.delete(id) > 0? true : false;
    }

    @Override
    public Boolean update(ProcessTemplate processTemplate) {
        if(processTemplate.getId() != 0){
            return processTemplateMapper.update(processTemplate) > 0? true : false;
        }
        throw new UserOperateException("操作异常：更新无id！");
    }

    @Override
    public Boolean uploadProcessTemplate(MultipartFile file) {
        try {
            // 获取类路径找到放到的路径下面
            String path = ResourceUtils.getURL("classpath:").getPath();
            String absolutePath = new File(path).getAbsolutePath();

            // 生成一下上传文件的目录
            File process = new File(path + "/processes/" );
            if(!process.exists()){
                process.mkdir();
            }

            // 保存文件
            String originalFilename = file.getOriginalFilename();
            File fileToPut = new File(path + "/processes/" + originalFilename);
            file.transferTo(fileToPut);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Boolean publish(Long id) {
        ProcessTemplate processTemplate = getProcessTemplateById(id);
        String processDefinitionPath = processTemplate.getProcessDefinitionPath();
        if(StringUtils.hasLength(processDefinitionPath)){
            processService.deployByZip(processDefinitionPath);
        }
        // 将activity流程图zip文件部署到数据库中

        // 1代表已经发布了
        processTemplate.setStatus(1);
        this.update(processTemplate);
        return true;
    }
}
