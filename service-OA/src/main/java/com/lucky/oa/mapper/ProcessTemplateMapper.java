package com.lucky.oa.mapper;

import com.lucky.model.process.ProcessTemplate;
import com.lucky.model.process.ProcessType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lucky
 * @create 2023-05-07 20:22
 */
@Mapper
public interface ProcessTemplateMapper {

    // 分页查询
    List<ProcessTemplate> list();

    // 根据id查询审批类型
    ProcessTemplate getProcessTemplateById(@Param("id")Long id);

    // 增删改查
    Integer add(ProcessTemplate processTemplate);

    Integer delete(@Param("id")Long id);

    Integer update(ProcessTemplate processTemplate);




}
