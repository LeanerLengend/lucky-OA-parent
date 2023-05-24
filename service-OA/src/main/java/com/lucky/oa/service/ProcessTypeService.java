package com.lucky.oa.service;

import com.github.pagehelper.PageInfo;
import com.lucky.model.process.ProcessType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lucky
 * @create 2023-05-07 16:22
 */
public interface ProcessTypeService {

    // 分页查询
    List<ProcessType> list();

    // 分页查询
    PageInfo<ProcessType> list(Integer page, Integer limit);

    // 根据id查询审批类型
    ProcessType getProcessTypeById(Long id);

    // 增删改查
    Boolean add(ProcessType processType);

    Boolean delete(Long id);

    Boolean update(ProcessType processType);


}
