package com.lucky.oa.activity;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author lucky
 * @create 2023-04-27 13:48
 */
public class MyTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        if(delegateTask.getName().equals("经理审批")){
            delegateTask.setAssignee("wangwu");
        }else if(delegateTask.getName().equals("人事审批")){
            delegateTask.setAssignee("chenyingxiang");
        }
    }

}
