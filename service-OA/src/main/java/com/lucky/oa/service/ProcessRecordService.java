package com.lucky.oa.service;

import com.lucky.model.process.ProcessRecord;
import com.lucky.vo.process.ProcessVo;

import java.util.List;

/**
 * @author lucky
 * @create 2023-05-13 9:36
 */
public interface ProcessRecordService {

    void record(Long processId,Integer status,String desc);


    // 根据id查询详细信息
    List<ProcessRecord> queryProcessRecordByProcessId(Long id);

}
