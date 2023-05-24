package com.lucky.oa.service;

import com.lucky.model.system.SysMenu;
import com.lucky.model.system.SysRole;
import com.lucky.oa.service.impl.SysMenuServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ChenYXCoding
 * @create 2023-03-06 20:41
 */
@Slf4j
@SpringBootTest
public class SysRoleServiceTest {

    @Autowired
    private SysRoleService sysRoleService;

    @Resource
    private  SysMenuService sysMenuService;



    @Test
    public void testService(){
        List<SysMenu> sysMenuListByParentId = sysMenuService.getSysMenuListByParentId(2L);
        SysMenuServiceImpl sysMenuService = (SysMenuServiceImpl) this.sysMenuService;
        List<SysMenu> sysMenus = sysMenuService.buildTree(sysMenuListByParentId);
        System.out.println(sysMenus);
    }

}
