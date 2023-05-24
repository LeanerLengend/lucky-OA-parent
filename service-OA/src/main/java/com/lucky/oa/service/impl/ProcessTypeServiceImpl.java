package com.lucky.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.common.exception.UserOperateException;
import com.lucky.model.process.ProcessType;
import com.lucky.oa.mapper.ProcessTypeMapper;
import com.lucky.oa.service.ProcessTypeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lucky
 * @create 2023-05-07 16:23
 */
@Service
public class ProcessTypeServiceImpl implements ProcessTypeService {

    @Resource
    private ProcessTypeMapper processTypeMapper;

    @Override
    public List<ProcessType> list() {
        return  processTypeMapper.list();
    }

    @Override
    public PageInfo<ProcessType> list(Integer page, Integer limit) {
        if(page != 0 && limit > 0){
            PageHelper.startPage(page,limit);
            return  new PageInfo<ProcessType>(processTypeMapper.list());
        }
        return null;
    }

    @Override
    public ProcessType getProcessTypeById(Long id) {
        return processTypeMapper.getProcessTypeById(id);
    }

    @Override
    public Boolean add(ProcessType processType) {
        String name = processType.getName();
        if(!StringUtils.hasLength(name)){
            throw new UserOperateException("请输入审批名称！");
        }
        return processTypeMapper.add(processType) > 0 ? true : false;
    }

    @Override
    public Boolean delete(Long id) {
        Integer delete = processTypeMapper.delete(id);
        return delete > 0 ? true : false;
    }

    @Override
    public Boolean update(ProcessType processType) {
        Integer update = processTypeMapper.update(processType);
        return update > 0 ? true : false;
    }
}
