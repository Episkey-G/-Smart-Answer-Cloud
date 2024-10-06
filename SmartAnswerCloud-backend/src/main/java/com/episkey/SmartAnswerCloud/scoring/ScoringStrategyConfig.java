package com.episkey.SmartAnswerCloud.scoring;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Copyright (c) 2024. All rights reserved.
 *
 * @author <a href="https://github.com/Episkey-G">Episkey</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ScoringStrategyConfig {

    /**
     * 应用类型
     *
     * @return 应用类型
     */
    int appType();

    /**
     * 评分策略
     *
     * @return 评分策略
     */
    int scoringStrategy();
}
