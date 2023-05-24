package com.lucky.oa.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lucky.model.process.Process;
import com.lucky.model.process.ProcessTemplate;
import com.lucky.model.system.SysUser;
import com.lucky.oa.service.ProcessService;
import com.lucky.oa.service.ProcessTemplateService;
import com.lucky.oa.service.SysUserService;
import com.lucky.oa.service.WeChatMessageService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author lucky
 * @create 2023-05-23 14:51
 */
@Service
public class WeChatMessageServiceImpl implements WeChatMessageService {

    @Resource
    private ProcessService processService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private ProcessTemplateService processTemplateService;

    @Resource
    private WxMpService wxMpService;

    @Value("${deploy.rear}")
    private String rear;

    @Value("${deploy.front}")
    private String front;

    /**
     * userId应该是下一个审批人
     * @param processId
     * @param userId 推送人Id
     * @param taskId
     */
    @Override
    public void pushPendingMessage(Long processId, Long userId, String taskId) {
        // 查询流程信息
        Process process = processService.getProcessById(processId);
        String description = process.getDescription();
        Date createTime = process.getCreateTime();
        String processCode = process.getProcessCode();
        // 查询出推送人的信息
        SysUser pusher = sysUserService.getById(userId);
        // 测试使用，如果是空的就设置为我自己的微信openId
        if(StringUtils.isEmpty(pusher.getOpenId()) ){
            pusher.setOpenId("o5PQ36GW59psGqoGLVLqoHeHnvT8");
        }

        // 申请人填写的模板信息
        ProcessTemplate processTemplate = processTemplateService.getProcessTemplateById(process.getProcessTemplateId());

        // 提交人信息
        SysUser submitter = sysUserService.getById(process.getUserId());
        // 编辑模板

        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(pusher.getOpenId()) // 被发送人的openId
                .templateId("V3ofURqGwfItH2yEnvS-1aI3-EQkWif1KgMWxxjPQ-A") // 微信公众号配置的模板id,zhe
                .url(front+"/#/show/"+processId+"/"+taskId) // 点击模板跳转到模板显示界面，注意是前端的页面
                .build();

        // 向模板中添加数据
        // 得到提交者在表单中添加的数据
        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formShowData = jsonObject.getJSONObject("formShowData");
        StringBuffer content = new StringBuffer();
        for (Map.Entry<String, Object> entry : formShowData.entrySet()) {
            // 添加值
            content.append(entry.getKey()).append(" : ").append(entry.getValue()).append("/n");
        }

        String first = submitter.getName()+" 提交 "+ processTemplate.getProcessTypeName() + "审批";
        templateMessage.addData(new WxMpTemplateData("first",first,"#272727"));

        templateMessage.addData(new WxMpTemplateData("keyword1",process.getProcessCode(),"#272727"));
        // 获取创建时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String createTimeToString = simpleDateFormat.format(process.getCreateTime());
        templateMessage.addData(new WxMpTemplateData("keyword2",createTimeToString,"#272727"));

        // 获取表单内容
        templateMessage.addData(new WxMpTemplateData("content",content.toString(),"#272727"));

        // 推送
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void pushProcessedMessage(Long processId, Long userId, Integer status) {

    }
}
