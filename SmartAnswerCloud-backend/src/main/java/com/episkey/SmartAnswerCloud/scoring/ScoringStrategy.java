package com.episkey.SmartAnswerCloud.scoring;

import com.episkey.SmartAnswerCloud.model.entity.App;
import com.episkey.SmartAnswerCloud.model.entity.UserAnswer;

import java.util.List;

/*
 * Copyright (c) 2024. All rights reserved.
 *
 * @author <a href="https://github.com/Episkey-G">Episkey</a>
 *
 * 评分策略
 */
public interface ScoringStrategy {

    /**
     * 执行评分
     *
     * @param choice 选择
     * @param app    应用
     * @return 评分结果
     * @throws Exception 异常
     */
    UserAnswer doScore(List<String> choice, App app) throws Exception;
}
