package com.lucky.oa.service;

import com.lucky.model.system.SysRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ChenYXCoding
 * @create 2023-03-06 20:41
 */
@Slf4j
@SpringBootTest
public class SysRoleServiceTest {

    @Autowired
    private SysRoleService sysRoleService;

    @Test
    public void testService(){
        SysRole byId = sysRoleService.getById(1);
        log.info(byId.toString());
    }

}
