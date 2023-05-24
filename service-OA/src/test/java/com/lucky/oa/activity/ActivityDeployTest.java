package com.lucky.oa.activity;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lucky
 * @create 2023-04-19 17:13
 */
@SpringBootTest
public class ActivityDeployTest {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private HistoryService historyService;

    @Resource
    private TaskService taskService;

    @Test
    public void testSuspend(){
        ProcessDefinition qingjia = repositoryService.createProcessDefinitionQuery().processDefinitionKey("qingjia").singleResult();
        boolean suspended = qingjia.isSuspended();
        if(suspended){
            repositoryService.activateProcessDefinitionById(qingjia.getId(),true,null);
            System.out.println("执行了激活！");
        }else {
            repositoryService.suspendProcessDefinitionById(qingjia.getId(),true,null);
            System.out.println("执行了挂起！");
        }
    }

    @Test
    public void testSuspendOneInstance(){
        // idd73a6629-e182-11ed-a967-fa89d2555a03
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId("5a014031-e183-11ed-b752-fa89d2555a03").singleResult();
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getName());
        if(processInstance.isSuspended()){
            runtimeService.activateProcessInstanceById(processInstance.getId());
        }else {
            runtimeService.suspendProcessInstanceById(processInstance.getId());
        }
    }
    
    @Test
    public void testAddBusinessKey(){
        String businessKey = "1";
        ProcessInstance qingjia = runtimeService.startProcessInstanceByKey("qingjia", businessKey);
        System.out.println(qingjia.getBusinessKey());
    }

    @Test
    public void testRemove(){
        repositoryService.deleteDeployment("16ad1803-deb6-11ed-a0ba-f889d2555a04");
    }


    @Test
    public void deployment(){
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia-gatway-bingxing.bpmn20.xml")
                .name("请假审批--by--gatway-bingxing")
                .deploy();
        String id = deploy.getId();
        String name = deploy.getName();
        System.out.println(id);
        System.out.println(name);
    }

    @Test
    public void beginInstance(){
        Map<String, Object> map = new HashMap<>();
        // ********************************
        map.put("day", 2);
        ProcessInstance jiaban = runtimeService.startProcessInstanceByKey("qingjia-gatway-bingxing",map);
        System.out.println("流程定义的id"+jiaban.getProcessDefinitionId());
        System.out.println("流程实例的id"+jiaban.getId());
        String name = jiaban.getName();
        System.out.println(name);
        System.out.println("当前活动的id"+jiaban.getActivityId());
    }

    @Test
    public void testGroupTask(){
        //查询组任务
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser("tom")//根据候选人查询
                .list();
        for (Task task : list) {
            System.out.println("----------------------------");
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    @Test
    public void claimTask(){
        //拾取任务,即使该用户不是候选人也能拾取(建议拾取时校验是否有资格)
        //校验该用户有没有拾取任务的资格
        Task task = taskService.createTaskQuery()
                .taskCandidateUser("tom")//根据候选人查询
                .singleResult();
        if(task!=null){
            //拾取任务
            taskService.claim(task.getId(), "tom");
            System.out.println("任务拾取成功");
        }
    }

    @Test
    public void testRepositoryService(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionAppVersion().desc().list();
        for (ProcessDefinition processDefinition : list) {
            System.out.println("流程定义 id="+processDefinition.getId());
            System.out.println("流程定义 name="+processDefinition.getName());
            System.out.println("流程定义 key="+processDefinition.getKey());
            System.out.println("流程定义 Version="+processDefinition.getVersion());
            System.out.println("流程部署ID ="+processDefinition.getDeploymentId());
        }
    }

    @Test
    public void testQueryTaskByUserName(){
        List<Task> zhangsan = taskService.createTaskQuery().taskAssignee("renshi").list();
        for(Task item : zhangsan){
            String person = (String) taskService.getVariable(item.getId(), "key");
            String key = (String)item.getProcessVariables().get("key");
            System.out.println(person);
            System.out.println(item.getBusinessKey());
            System.out.println("任务："+zhangsan);
            System.out.println("任务名称"+ item.getName());
            System.out.println(item.getAssignee());
            System.out.println(item.getTaskDefinitionKey());
        }
    }

    @Test
    public void testTakeTask(){
        List<Task> zhangsan = taskService.createTaskQuery().taskAssignee("bumenjingli").list();
        HashMap<String, Object> map = new HashMap<>();
        map.put("key","put be change");
        for(Task item : zhangsan){
            System.out.println("任务："+zhangsan);
            taskService.complete(item.getId(),map);
        }
    }
    
    @Test
    public void testQueryTask(){
        List<HistoricTaskInstance> over = historyService.createHistoricTaskInstanceQuery().taskAssignee("zhangsan").finished().list();
        for (HistoricTaskInstance item : over){
            System.out.println(item.getProcessDefinitionId());
            System.out.println(item.getName());
            System.out.println(item.getAssignee());
            System.out.println(item.getName());
        }
    }
}
