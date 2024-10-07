package com.episkey.SmartAnswerCloud.manager;

import com.episkey.SmartAnswerCloud.common.ErrorCode;
import com.episkey.SmartAnswerCloud.exception.BusinessException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/*
 * 智能问答管理器
 * Copyright (c) 2024. All rights reserved.
 *
 * @author <a href="https://github.com/Episkey-G">Episkey</a>
 */
@Component
public class AiManager {

    @Resource
    private ClientV4 clientV4;

    private static final float STABLE_TEMPERATURE = 0.05f;
    private static final float UNSTABLE_TEMPERATURE = 0.99f;

    /**
     * 不稳定同步请求智能问答接口(简化消息传递)
     *
     * @param systemMessage 系统消息
     * @param userMessage   用户消息
     * @return 智能问答结果
     */
    public String doSyncUnstableRequest(String systemMessage, String userMessage) {
        // 构建请求
        return doRequest(systemMessage, userMessage, Boolean.FALSE, UNSTABLE_TEMPERATURE);
    }

    /**
     * 稳定同步请求智能问答接口(简化消息传递)
     *
     * @param systemMessage 系统消息
     * @param userMessage   用户消息
     * @return 智能问答结果
     */
    public String doSyncStableRequest(String systemMessage, String userMessage) {
        // 构建请求
        return doRequest(systemMessage, userMessage, Boolean.FALSE, STABLE_TEMPERATURE);
    }

    /**
     * 同步请求智能问答接口(简化消息传递)
     *
     * @param systemMessage 系统消息
     * @param userMessage   用户消息
     * @param temperature 温度
     * @return 智能问答结果
     */
    public String doSyncRequest(String systemMessage, String userMessage, Float temperature) {
        // 构建请求
        return doRequest(systemMessage, userMessage, Boolean.FALSE, temperature);
    }

    /**
     * 通用请求智能问答接口(简化消息传递)
     *
     * @param systemMessage 系统消息
     * @param userMessage   用户消息
     * @param stream      是否流式请求
     * @param temperature 温度
     * @return 智能问答结果
     */
    public String doRequest(String systemMessage, String userMessage, Boolean stream, Float temperature) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        ChatMessage systemChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage);
        chatMessageList.add(systemChatMessage);
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), userMessage);
        chatMessageList.add(userChatMessage);
        // 构建请求
        return doRequest(chatMessageList, stream, temperature);
    }

    /**
     * 通用请求智能问答接口
     *
     * @param chatMessageList 消息列表
     * @param stream          是否流式请求
     * @param temperature     温度
     * @return 智能问答结果
     */
    public String doRequest(List<ChatMessage> chatMessageList, Boolean stream, Float temperature) {
        // 构建请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .temperature(temperature)
                .invokeMethod(Constants.invokeMethod)
                .messages(chatMessageList)
                .build();
        try {
            ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
            return invokeModelApiResp.getData().getChoices().get(0).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }
}
