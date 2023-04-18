package com.lucky.oa.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.model.system.SysRole;
import com.lucky.oa.mapper.SysRoleMapper;
import com.lucky.oa.service.SysRoleService;
import com.lucky.vo.system.SysRoleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author ChenYXCoding
 * @create 2023-03-06 20:37
 */
@Service
public class SysRoleServiceImpl  implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> getAllSysRole() {
        return sysRoleMapper.getAllSysRole();
    }

    @Override
    public PageInfo<SysRole> page(SysRoleQueryVo sysRoleQueryVo, Integer page,Integer limit) {
        Page<Object> objects = PageHelper.startPage(page, limit);
        List<SysRole> list = sysRoleMapper.page(sysRoleQueryVo);
        return  new PageInfo<SysRole>(list);
    }

    @Override
    public Boolean save(SysRole sysRole) {
        int result = sysRoleMapper.save(sysRole);
        return result > 0 ? true : false;
    }

    @Override
    public SysRole getById(Integer id) {
        return sysRoleMapper.getById(id);
    }

    @Override
    public Boolean removeById(Integer id) {
        return sysRoleMapper.removeById(id) >= 1 ? true :false;
    }

    @Override
    public Boolean removeByIds(List<Integer> ids) {
        return sysRoleMapper.removeByIds(ids) >= 1 ? true : false;
    }

    public Boolean updateById(SysRole SysRole){
        return sysRoleMapper.updateById(SysRole) >= 1 ? true :false;
    }
}
