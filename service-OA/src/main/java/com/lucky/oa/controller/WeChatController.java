package com.lucky.oa.controller;

import com.lucky.common.jwt.JwtHelper;
import com.lucky.common.result.Result;
import com.lucky.model.system.SysUser;
import com.lucky.oa.service.SysUserService;
import com.lucky.vo.wechat.BindPhoneVo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author lucky
 * @create 2023-05-21 21:06
 */
// 注意这里写的是controller，用重定向做一些页面跳转
@Controller
@RequestMapping("/admin/wechat")
@Slf4j
// 从9090 --> 8080跨域了，所以要加这个注解
@CrossOrigin
public class WeChatController {

    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    @Value("${wechat.replaceStr}")
    private String replaceStr;

    @Resource
    private WxMpService wxMpService;

    @Resource
    private SysUserService sysUserService;

    /**
     *
     * @param returnUrl 用户想要跳转到路径
     * @param request
     * @return
     */
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl, HttpServletRequest request) {
        // TODO 挖坑oauth2认证
        // TODO 为什么需要转换一下呢#,字符串
        /**
         * ①进行微信授权，设置获取用户信息地址
         * ②如果没有授权，就跳转到 userInfo
         *
         * 这里是调用方法来实现的,
         * 第一个参数:授权路径，在哪个路径获取微信信息
         * 第二个参数:固定值，授权类型wxConsts.OAuth2sdope . SNSAPT_USERINFO
         * 第三个参数:授权成功之后，跳转路径' guiguoa’转换成‘'#′
         */
        String redirectUrl = null;
        try {
            redirectUrl = wxMpService.getOAuth2Service()
                    .buildAuthorizationUrl(this.userInfoUrl, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl.replace(replaceStr, "#"),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "redirect:" + redirectUrl;
    }

    /**
     * 执行完authorize自动跳转到userInfo当中去，自动调用这个方法
     * @param code 微信提供的临时验证码
     * @param returnUrl
     * @return
     * @throws Exception
     */
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) throws Exception {
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        WxOAuth2UserInfo userInfo = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);
        String openId = accessToken.getOpenId();
        SysUser sysUser = sysUserService.getSysUserByOpenId(openId);
        // 说明当前系统中没有该用户,TODO 新增用户
        String token = "";
        if(sysUser != null){
            token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        } else {
//            不存在则新增用户,用户名是默认的用户名
            SysUser newUser = new SysUser();
            newUser.setOpenId(openId);
            String tempName = UUID.randomUUID().toString().substring(0,28);
            newUser.setUsername(tempName);
            sysUserService.save(newUser);
            token = JwtHelper.createToken(newUser.getId(), newUser.getUsername());
        }
        // 纯地址没有参数，用？拼接字符串
        if(!returnUrl.contains("?")) {
            return "redirect:" + returnUrl + "?token=" + token + "&openId=" + openId;
        } else {
            // 有其他参数加上&来拼接参数
            return "redirect:" + returnUrl + "&token=" + token + "&openId=" + openId;
        }
    }


    @PostMapping("/bindPhone")
    @ResponseBody
    public Result bindPhone(@RequestBody @Valid BindPhoneVo bindPhoneVo) {
        // 如果存在则绑定手机号
        SysUser sysUser = sysUserService.getByPhoneNum(bindPhoneVo.getPhone());
        String token = "";
        // 手机号不在本系统
        if(sysUser != null){
            sysUser.setOpenId(bindPhoneVo.getOpenId());
            sysUserService.updateById(sysUser);
            token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        } else {
            //不存在则新增用户,用户名是默认的用户名
            sysUser = new SysUser();
            sysUser.setOpenId(bindPhoneVo.getOpenId());
            sysUser.setPhone(bindPhoneVo.getPhone());
            String randomUserName = UUID.randomUUID().toString().substring(10);
            sysUser.setUsername("wixin-"+ randomUserName);
            sysUserService.save(sysUser);
            token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        }
        return Result.ok(token);
    }
}
