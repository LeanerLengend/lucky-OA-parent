package com.lucky.oa.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lucky.model.system.SysRole;
import com.lucky.oa.LuckyOAApplication;
import com.lucky.oa.mapper.SysRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ChenYXCoding
 * @create 2023-03-06 19:14
 */
@Slf4j
@SpringBootTest
public class TestSysRoleMapper {

    @Autowired
    SysRoleMapper mapper;

    @Test
    public void mySelect(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("嗨嗨嗨！66");
        sysRole.setId(9l);
        log.info("查询结果是：{}",mapper.selectList(null));
    }

    @Test
    public void queryByWrapper(){
        QueryWrapper<SysRole> condition = new QueryWrapper<>();
        condition.like("role_name","嗨嗨嗨");
        log.info("查询结果是：{}",mapper.selectList(condition));
    }

    @Test
    public void queryByWrapperLamba(){
        LambdaQueryWrapper<SysRole> condition = new LambdaQueryWrapper<>();
        condition.like(SysRole::getRoleName,"嗨嗨嗨");
        log.info("查询结果是：{}",mapper.selectList(condition));
    }


}
