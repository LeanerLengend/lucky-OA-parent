package com.lucky.oa.service;

/**
 * @author lucky
 * @create 2023-05-21 10:34
 */
public interface WeChatMessageService {

    // 推送给审批人审批消息
    void pushPendingMessage(Long processId, Long userId, String taskId);


    // 推送给提交者，是否通过
    void pushProcessedMessage(Long processId, Long userId, Integer status);

}
