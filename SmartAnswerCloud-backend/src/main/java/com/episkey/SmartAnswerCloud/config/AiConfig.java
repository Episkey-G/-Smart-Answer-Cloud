package com.episkey.SmartAnswerCloud.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * Copyright (c) 2024. All rights reserved.
 *
 * @author <a href="https://github.com/Episkey-G">Episkey</a>
 */
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiConfig {

    /**
     * API Key 需要从开放平台获取
     */
    private String apiKey;

    /**
     * 获取 AI 客户端
     *
     * @return
     */
    @Bean
    public ClientV4 getClientV4() {
        return new ClientV4.Builder(apiKey).build();
    }
}
