package com.lucky.oa.service.impl;

import com.lucky.model.process.ProcessRecord;
import com.lucky.model.system.SysUser;
import com.lucky.oa.mapper.ProcessMapper;
import com.lucky.oa.mapper.ProcessRecordMapper;
import com.lucky.oa.service.ProcessRecordService;
import com.lucky.oa.service.ProcessService;
import com.lucky.oa.service.SysUserService;
import com.lucky.springsecurity.custom.LoginUserInfoHelper;
import com.lucky.vo.process.ProcessVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lucky
 * @create 2023-05-13 9:47
 */
@Service
public class ProcessRecordServiceImpl implements ProcessRecordService {

    @Resource
    private ProcessRecordMapper processRecordMapper;

    @Resource
    private SysUserService sysUserService;


    @Override
    public List<ProcessRecord> queryProcessRecordByProcessId(Long id) {
        return processRecordMapper.getProcessRecordByProcessId(id);
    }

    /**
     *可以从当前的线程中取到用户的id，因为凡是你登录这个系统就会每一次授权认证给你放到当前线程当中去
     */
    @Override
    public void record(Long processId, Integer status, String desc) {
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setDescription(desc);
        processRecord.setStatus(status);
        // 从当前的线程中取到
        SysUser user = sysUserService.getById(LoginUserInfoHelper.getUserId());
        String name = user.getName();
        processRecord.setOperateUserId(user.getId());
        processRecord.setOperateUser(name);
        processRecordMapper.save(processRecord);
    }
}
