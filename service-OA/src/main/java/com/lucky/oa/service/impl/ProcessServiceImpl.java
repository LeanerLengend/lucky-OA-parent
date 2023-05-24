package com.lucky.oa.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.common.exception.UserOperateException;
import com.lucky.model.process.Process;
import com.lucky.model.process.ProcessRecord;
import com.lucky.model.process.ProcessTemplate;
import com.lucky.model.process.ProcessType;
import com.lucky.model.system.SysUser;
import com.lucky.oa.mapper.ProcessMapper;
import com.lucky.oa.service.*;
import com.lucky.springsecurity.custom.LoginUserInfoHelper;
import com.lucky.vo.process.ApprovalVo;
import com.lucky.vo.process.ProcessFormVo;
import com.lucky.vo.process.ProcessQueryVo;
import com.lucky.vo.process.ProcessVo;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @author lucky
 * @create 2023-05-09 19:32
 */
@Service
public class ProcessServiceImpl implements ProcessService {

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ProcessRecordService processRecordService;

    @Resource
    private ProcessTemplateService processTemplateService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Resource
    private WeChatMessageService weChatMessageService;

    @Override
    public PageInfo<ProcessVo> findStarted(Long page, Long limit) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        PageHelper.startPage(page.intValue(),limit.intValue());
        List<ProcessVo> processVoList = processMapper.page(processQueryVo);
        return new PageInfo<ProcessVo>(processVoList);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<ProcessVo> findProcessed(Long page, Long limit) {
        //张三已处理过的历史任务
        PageHelper.startPage(page.intValue(),limit.intValue());
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();

        List<ProcessVo> listToUse = new ArrayList<>();
        for (HistoricTaskInstance historicTaskInstance : list) {
            String processInstanceId = historicTaskInstance.getProcessInstanceId();
            Process process = processMapper.getProcessByProcessInstanceId(processInstanceId);
            ProcessVo processVo = processMapper.queryProcessVoByProcessId(process.getId());
            listToUse.add(processVo);
        }



        return new PageInfo<>(listToUse);
    }

    /**
     *
     * @param approvalVo
     */
    @Transactional
    @Override
    public void approve(ApprovalVo approvalVo) {
        this.isIllegalApprovalVo(approvalVo);
        //默认就是同意了
        taskService.complete(approvalVo.getTaskId());
        // 修改process表，process表修改：修改description，修改current_audior ,判断状态，update状态
        // 这里需要查一下，给一些要不然直接new，一些值就给覆盖了！！
        Process process = this.getProcessById(approvalVo.getProcessId());
        List<Task> taskList = this.getTaskListByInstanceId(process.getProcessInstanceId());
        String recordDesc = "";
        // 任务完成
        if(CollectionUtils.isEmpty(taskList)){
            process.setStatus(2);
            process.setDescription("审批通过");
            // 这个为暂时待定
            //process.setCurrentAuditor(LoginUserInfoHelper.getUsername());
        }else {
            // 下一个审批人
            List<String> auditors = new ArrayList<>();
            for (Task task : taskList) {
                String assignee = task.getAssignee();
                String name = sysUserService.getUserEntityByUserName(assignee).getName();
                auditors.add(name);
            }
            process.setStatus(1);
            process.setDescription("等待"+ StringUtils.join(auditors,",") +"审批");
        }
        processMapper.update(process);
        // 在记录表中添加数据
        processRecordService.record(approvalVo.getProcessId(),process.getStatus(),"审批通过！");
    }


    /**
     * 审批不通过
     * @param approvalVo
     */
    @Transactional
    @Override
    public void reject(ApprovalVo approvalVo) {
        String taskId = approvalVo.getTaskId();
        this.isIllegalApprovalVo(approvalVo);
        Process process = this.getProcessById(approvalVo.getProcessId());
        process.setStatus(-1);
        process.setDescription("审批驳回！");
        String recordDesc = "审批驳回！";
        processMapper.update(process);
        // 在记录表中添加数据
        processRecordService.record(approvalVo.getProcessId(),process.getStatus(),"审批不通过！");

        //  当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        // 并行任务可能为null
        if(CollectionUtils.isEmpty(endEventList)) {
            return;
        }
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        //  清理活动方向
        currentFlowNode.getOutgoingFlows().clear();

        //  建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        //  当前节点指向新的方向
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //  完成当前任务
        taskService.complete(task.getId());

    }
    /**
     * 判断参数ApprovalVo 是否合法
     */
    private void isIllegalApprovalVo(ApprovalVo approvalVo){
        if(approvalVo.getProcessId() == 0L || StrUtil.isEmpty(approvalVo.getTaskId())){
            throw new UserOperateException("审批流程执行，客户端参数异常！");
        }
    }

    @Override
    public PageInfo<ProcessVo> list(ProcessQueryVo processQueryVo, Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<ProcessVo> list = processMapper.page(processQueryVo);
        return new PageInfo<ProcessVo>(list);
    }

    @Override
    public void deployByZip(String zipPath) {
        // 定义zip输入流
        InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(zipPath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        // 流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
    }

    @Override
    public List<ProcessType> getAllProcessTypeAndTemplate() {
        return  processMapper.getAllProcessTypeAndTemplate();
    }

    @Override
    public Integer save(Process process) {
        return processMapper.save(process);
    }

    @Override
    public Integer update(Process process) {
        return processMapper.update(process);
    }

    @Override
    public boolean startProcess(@Valid ProcessFormVo processFormVo) {
        // 获取开启实例用户信息
        SysUser user = sysUserService.getById(LoginUserInfoHelper.getUserId());
        // 查询模板
        ProcessTemplate processTemplate = processTemplateService.getProcessTemplateById(processFormVo.getProcessTemplateId());
        Process process = new Process();
        // 将processType中有的属性值赋值到空对象process中，属性值相同才会复制，省略了set方法
        BeanUtils.copyProperties(processFormVo,process);
        process.setStatus(1);
        process.setProcessCode(UUID.randomUUID().toString());
        process.setUserId(user.getId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(user.getName() + "发起" + processTemplate.getName() + "申请");
        // 在业务表oa_process中保存一下业务信息，保存一下信息,也就是新增的ip属性值
        processMapper.save(process);
        // 1.启动流程实例
        // 1.1流程定义的key
        // 1.2业务的key 也就是processId
        // 1.3 流程的变量
        String processDefinitionKey = processTemplate.getProcessDefinitionKey();
        // 目的和activity进行关联
        Long processId = process.getId();
        // 1.3 流程变量的值，把他变成一个json字符串
        JSONObject formValues = JSON.parseObject(processFormVo.getFormValues());
        // 默认表单的根值就是
        JSONObject formData = formValues.getJSONObject("formData");
        Map<String ,Object> map = new HashMap<>();
        Map<String ,Object> variable = new HashMap<>();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            // 把键值对放到map值当中
            map.put(entry.getKey(),entry.getValue());
        }
        // 这里考虑到后面流程需要携带非常多的参数，这些都是data，所以把他们归属到data这一类
        variable.put("data",map);
        // 启动流程实例,这个busnesskey就是在process表中生成的记录的id值
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, String.valueOf(processId), variable);
        // 查询任务列表，获取所有的审批信息
        List<Task> taskList = this.getTaskListByInstanceId(processInstance.getId());
        List<String> auditors = new ArrayList<>();
        for(Task task : taskList){
            // 这是查询的用户名
            String auditor = task.getAssignee();
            SysUser sysUser = sysUserService.getUserEntityByUserName(auditor);
            // 查询用户姓名
            auditors.add(sysUser.getName());
            // 给审批人推送消息,重头戏
            weChatMessageService.pushPendingMessage(processId,sysUser.getId(),task.getId());
        }
        // 业务和流程进行关联，保存相关审批的信息,就是更新一下启动流程后的信息
        process.setProcessInstanceId(processInstance.getProcessInstanceId());
        // 把数组里面的值合起来，用分隔符
        process.setDescription("等待"+ StringUtils.join(auditors,",") +"审批");
        this.update(process);
        processRecordService.record(processId,process.getStatus(),process.getDescription());
        return true;
    }

    /**
     * 根据流程实例id查询任务，有可能有多个任务返回
     * @param processInstanceId
     * @return 当前处理人列表
     */
    public List<Task> getTaskListByInstanceId(String processInstanceId){
        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }

    /**
     *查询代办任务,这里目的在于演示一下在ativity中如何分页
     * ~~~~~~~~~~可以去除分页
     * @return
     */
    @Override
    public PageInfo<ProcessVo> queryBackLogTask(Long page,Long limit) {
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = sysUserService.getById(userId);
        PageHelper.startPage(page.intValue(),limit.intValue());
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(sysUser.getUsername())
                .orderByTaskCreateTime()
                .desc()   // 原生分页写法，在activity中必须这样
                .listPage(((page.intValue()-1)*limit.intValue()),limit.intValue());
        // 把Task信息转换成ProcessVo进行显示
        List<ProcessVo> processVoList= new ArrayList<ProcessVo>();
        // 查询出用户的代办任务
        for(Task task : taskList){
            String processInstanceId = task.getProcessInstanceId();
            String businessKey = task.getBusinessKey();
            if(businessKey == null || businessKey.equals("")){
                continue;
            }
            long key = Long.parseLong(businessKey);
            ProcessVo processVo = processMapper.queryProcessVoByProcessId(key);
            processVo.setTaskId(task.getId());
            processVoList.add(processVo);
        }
        return new PageInfo<>(processVoList);
    }

    /**
     * 待审批：显示某个用户申请项的详情
     * 判断用户是否可以审批成功
     */
    @Override
    public Map<String, Object> show(Long id) {
        ProcessVo processVo = processMapper.queryProcessVoByProcessId(id);
        // 查询审批记录
        List<ProcessRecord> processRecords = processRecordService.queryProcessRecordByProcessId(processVo.getId());
        // 查询模板，需要获取填写的参数等等
        ProcessTemplate processTemplate = processTemplateService.getProcessTemplateById(processVo.getProcessTemplateId());
        // 判断是否可以审批
        // 获取当前流程到哪一个人审批
        Boolean isApprove = false;
        for (Task task : this.getTaskListByInstanceId(processVo.getProcessInstanceId())) {
            if(task.getAssignee().equals(LoginUserInfoHelper.getUsername())){
                isApprove = true;
            }
        }
        Map<String ,Object> map = new HashMap<>();
        map.put("process", processVo);
        map.put("processRecordList", processRecords);
        map.put("processTemplate", processTemplate);
        map.put("isApprove", isApprove);
        return map;
    }

    @Override
    public Process getProcessById(Long id) {
        return processMapper.getProcessById(id);
    }

}
