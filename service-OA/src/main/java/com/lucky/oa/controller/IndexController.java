package com.lucky.oa.controller;
import com.lucky.common.exception.UserOperateException;
import com.lucky.common.jwt.JwtHelper;
import com.lucky.common.result.Result;
import com.lucky.model.system.SysUser;
import com.lucky.oa.service.SysMenuService;
import com.lucky.oa.service.SysUserService;
import com.lucky.vo.system.LoginVo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenYXCoding
 * @create 2023-03-11 16:50
 */
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){

        if(StringUtils.isEmpty(loginVo.getUsername()))
            throw new UserOperateException("请输入用户名！");

        if(StringUtils.isEmpty(loginVo.getPassword()))
            throw new UserOperateException("请输入密码！");

        String token = sysUserService.login(loginVo);

        Map<String, Object> map = new HashMap<>();
        map.put("token",token);

        return Result.ok(map);
    }


    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        String token = request.getHeader("token");

        if(token == null){
            throw  new UserOperateException("请先登录！");
        }
        Map<String, Object> map = sysUserService.info(token);
        return Result.ok(map);
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
}
