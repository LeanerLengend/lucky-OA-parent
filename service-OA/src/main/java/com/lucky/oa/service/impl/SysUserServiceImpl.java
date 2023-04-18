package com.lucky.oa.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.common.exception.UserOperateException;
import com.lucky.common.jwt.JwtHelper;
import com.lucky.common.utils.MD5Helper;
import com.lucky.model.system.SysMenu;
import com.lucky.model.system.SysRole;
import com.lucky.model.system.SysUser;
import com.lucky.oa.mapper.SysRoleMapper;
import com.lucky.oa.mapper.SysUserMapper;
import com.lucky.oa.mapper.SysUserRoleMapper;
import com.lucky.oa.service.SysMenuService;
import com.lucky.oa.service.SysRoleService;
import com.lucky.oa.service.SysUserRoleService;
import com.lucky.oa.service.SysUserService;
import com.lucky.vo.system.LoginVo;
import com.lucky.vo.system.RouterVo;
import com.lucky.vo.system.SysUserQueryVo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChenYXCoding
 * @create 2023-03-16 20:09
 */
@Service
public class SysUserServiceImpl  implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 为用户分配角色
     */
    @Override
    public Boolean assignRoles(Long userId, List<Long> roleIds) {
        // 移除原有的角色信息
        sysUserRoleService.removeRolesForUserById(userId);
        // 添加角色信息
        Integer result = sysUserRoleService.assignRoles(userId, roleIds);
        return result > 0 ? true : false;
    }

    /**
     * 返回map集合，存储两部分数据
     * 1、查询完所有的角色信息
     * 2、用户有的角色
     * @param id
     * @return
     */
    @Override
    public Map<String ,Object> getRolesByUserId(Long id) {
        SysUser roleList = sysUserRoleService.getRolesByUserId(id);
        List<SysRole> list = null;
        List<SysRole> haveRoles = new ArrayList<>();

        if(roleList != null){
            list = roleList.getRoleList();
            for (SysRole x : list){
                haveRoles.add(x);
            }
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("assginRoleList",haveRoles);
        map.put("allRolesList",sysRoleService.getAllSysRole());
        return map;
    }

    @Override
    public PageInfo<SysUser> page(SysUserQueryVo sysUserQueryVo ,Integer page,Integer limit) {
        PageHelper.startPage(page,limit);
        List<SysUser> list = sysUserMapper.page(sysUserQueryVo);
        return  new PageInfo<>(list);
    }

    @Override
    public Boolean save(SysUser SysUser) {
        // 把密码加密一下然后再保存
        if(SysUser.getPassword() != null){
            SysUser.setPassword(MD5Helper.encrypt(SysUser.getPassword()));
        }
        int save = sysUserMapper.save(SysUser);
        if(save > 0){
            return true;
        }
        return false;
    }

    @Override
    public SysUser getById(Long id) {
        return sysUserMapper.getById(id);
    }

    @Override
    public Boolean removeById(Integer id) {
        return sysUserMapper.removeById(id) > 0 ? true : false;
    }

    @Override
    public Boolean removeByIds(List<Integer> ids) {
        return sysUserMapper.removeByIds(ids) > 0 ? true : false;
    }

    @Override
    public Boolean updateById(SysUser sysUser) {
        Long id = sysUser.getId();
        if(id != null && id != 0){
            int result = sysUserMapper.updateById(sysUser);
            if(result > 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public int enableOrDisable(Long userId, Integer status) {
        int result ;
        if(status == 0){
            result = sysUserMapper.enableOrDisable(userId,0);
        }else {
            result = sysUserMapper.enableOrDisable(userId,1);
        }
        return result;
    }

    @Override
    public String login(LoginVo loginVo) {
        if(this.checkUsername(loginVo.getUsername()) == 0)
            throw  new UserOperateException("用户不存在，请检查用户名输入是否正确！");

        SysUser user = sysUserMapper.getUserByUsernameAndPWD(loginVo.getUsername(), MD5Helper.encrypt(loginVo.getPassword()) );

        if(user == null)
            throw  new UserOperateException("用户名或密码输入错误！");
        if(user.getStatus() == 1)
            throw  new UserOperateException("用户数据发生异常，暂停使用！");
        return JwtHelper.createToken(user.getId(), user.getUsername());
    }

    @Override
    public Integer checkUsername(String username) {
        return sysUserMapper.getUserByUserName(username);
    }

    /**
     * 已知是用户的id ，通过用户的id返回用户的菜单，包括能操作的列表等等
     * @param token
     * @return
     */
    @Override
    public Map<String, Object> info(String token) {

        // 获取用户id
        Long userId = JwtHelper.getUserId(token);

        // 获取用户相关信息
        SysUser user = this.getById(userId);

        List<RouterVo> routerList = sysMenuService.getRouterMenuByUserId(userId);
        List<String> permsList = sysMenuService.getRouterMenuPermsByUserId(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name",user.getName());
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");

        //返回用户可以操作菜单
        map.put("routers",routerList);

        //返回用户可以操作按钮
        map.put("buttons",permsList);

        return map;
    }

    @Override
    public SysUser getUserEntityByUserName(String username) {
        return sysUserMapper.getUserEntityByUserName(username);
    }
}
