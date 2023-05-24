package com.lucky.oa.mapper;

import com.lucky.model.process.ProcessRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lucky
 * @create 2023-05-13 9:34
 */
@Mapper
public interface ProcessRecordMapper {

    // 新增记录条数
    void save(ProcessRecord processRecord);

    // 根据id查询processRecord,processRecord可能有多个
    List<ProcessRecord> getProcessRecordByProcessId(Long id);


}
