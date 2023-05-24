package com.lucky.oa.mapper;

import com.github.pagehelper.PageInfo;
import com.lucky.model.process.Process;
import com.lucky.model.process.ProcessType;
import com.lucky.vo.process.ProcessQueryVo;
import com.lucky.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lucky
 * @create 2023-05-09 16:54
 */
@Mapper
public interface ProcessMapper {

    // 条件查询 name : 模糊查询名称 ，
    List<ProcessVo> page(ProcessQueryVo processQueryVo);

    // 查询审批类型，审批类型包含的审批模板
    List<ProcessType> getAllProcessTypeAndTemplate();

    // 添加审批信息
    Integer save(Process process);

    // 修改审批信息
    Integer update(Process process);

    // 根据id查询详细信息
    ProcessVo queryProcessVoByProcessId(Long id);

    // 根据id查询指定的流程信息
    Process getProcessById(Long id);

    // 根据指定的流程实例id查询流程
    Process getProcessByProcessInstanceId(String instanceId);
}
