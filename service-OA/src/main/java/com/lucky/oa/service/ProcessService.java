package com.lucky.oa.service;

import com.github.pagehelper.PageInfo;
import com.lucky.model.process.Process;
import com.lucky.model.process.ProcessTemplate;
import com.lucky.model.process.ProcessType;
import com.lucky.vo.process.ApprovalVo;
import com.lucky.vo.process.ProcessFormVo;
import com.lucky.vo.process.ProcessQueryVo;
import com.lucky.vo.process.ProcessVo;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

/**
 * @author lucky
 * @create 2023-05-09 19:31
 */
public interface ProcessService {

    PageInfo<ProcessVo> findStarted(Long page,Long limit);

    PageInfo<ProcessVo> findProcessed(Long page,Long limit);

    // 审批人同意审批
    void approve(ApprovalVo approvalVo);

    // 审批人不同意
    void reject(ApprovalVo approvalVo);

    // 分页查询
    PageInfo<ProcessVo> list(ProcessQueryVo processQueryVo, Integer page, Integer limit);

    // 部署流程，因为部署的时候是根据文件的zip文件地址进行部署的
    void deployByZip(String zipPath);

    // 查询审批类型，审批类型包含的审批模板
    List<ProcessType> getAllProcessTypeAndTemplate();

    // 启动流程实例
    boolean startProcess(ProcessFormVo processFormVo);

    // 添加
    Integer save(Process process);

    // 修改审批信息
    Integer update(Process process);

    // 查询待办任务
    PageInfo<ProcessVo> queryBackLogTask(Long page,Long limit);

    // 要返回多种信息，所以说需要一个map集合参数的返回值
    // 返回基础信息
    // 返回谁审批了信息
    Map<String,Object> show(Long id);


    // 根据id查询指定的流程信息
    Process getProcessById(Long id);
}
