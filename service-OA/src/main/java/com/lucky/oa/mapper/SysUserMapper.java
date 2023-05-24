package com.lucky.oa.mapper;
import com.lucky.model.system.SysUser;
import com.lucky.vo.system.SysUserQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author ChenYXCoding
 * @create 2023-03-16 20:07
 */
@Mapper
public interface SysUserMapper  {

    // 查找手机号
    SysUser getPhoneNum(String phone);

    // 根据微信的openId查询用户
    SysUser getSysUserByOpenId(String openId);

    List<SysUser> page(@Param("sysUserQueryVo") SysUserQueryVo sysUserQueryVo);

    // 添加系统用户
    int save( SysUser SysUser);

    // 根据id查询系统用户
    SysUser getById(Long id);

    // 根据id删除系统用户
    int removeById(Integer id);

    // 根据id批量删除用户
    int removeByIds( List<Integer> ids);

    // 更新用户
    int updateById(SysUser sysUser);

    // 启用或者禁用用户
    int enableOrDisable(@Param("userId")Long userId,@Param("status")Integer status);

    // 根据用户名密码查询数据库中是否存在用户
    SysUser getUserByUsernameAndPWD(@Param("username")String username,@Param("password")String  password);

    // 根据用户名查询是否有当前用户
    Integer getUserByUserName(@Param("username")String username);

    // 根据用户名查询用户
    SysUser getUserEntityByUserName(@Param("username")String username);

}
