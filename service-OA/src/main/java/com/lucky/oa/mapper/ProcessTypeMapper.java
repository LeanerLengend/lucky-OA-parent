package com.lucky.oa.mapper;

import com.lucky.model.process.ProcessType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lucky
 * @create 2023-05-07 16:04
 */
@Mapper
public interface ProcessTypeMapper {

    // 分页查询
    List<ProcessType> list();

    // 根据id查询审批类型
    ProcessType getProcessTypeById(@Param("id")Long id);

    // 增删改查
    Integer add(ProcessType processType);

    Integer delete(@Param("id")Long id);

    Integer update(ProcessType processType);
}
