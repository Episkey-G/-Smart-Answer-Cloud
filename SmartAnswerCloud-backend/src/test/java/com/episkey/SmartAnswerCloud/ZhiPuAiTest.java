package com.episkey.SmartAnswerCloud;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2024. All rights reserved.
 *
 * @author <a href="https://github.com/Episkey-G">Episkey</a>
 */
@SpringBootTest
public class ZhiPuAiTest {

    @Resource
    private ClientV4 clientV4;

    @Test
    public void test() {
        // 初始化客户端
        // 构建请求
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "http和https的区别，说明一条即可");
        messages.add(chatMessage);
//        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        System.out.println("model output:" + invokeModelApiResp.toString());
    }
}
