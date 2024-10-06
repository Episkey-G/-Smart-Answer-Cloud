package com.episkey.SmartAnswerCloud.scoring;

import com.episkey.SmartAnswerCloud.common.ErrorCode;
import com.episkey.SmartAnswerCloud.exception.BusinessException;
import com.episkey.SmartAnswerCloud.model.entity.App;
import com.episkey.SmartAnswerCloud.model.entity.UserAnswer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/*
 * Copyright (c) 2024. All rights reserved.
 *
 * @author <a href="https://github.com/Episkey-G">Episkey</a>
 */
@Service
public class ScoringStrategyExecutor {

    // 策略列表
    @Resource
    private List<ScoringStrategy> scoringStrategyList;

    /**
     * 执行评分
     *
     * @param choiceList 选择列表
     * @param app        应用
     * @return 评分结果
     * @throws Exception 异常
     */
    public UserAnswer doScore(List<String> choiceList, App app) throws Exception {
        Integer appType = app.getAppType();
        Integer appScoringStrategy = app.getScoringStrategy();
        if (appType == null || appScoringStrategy == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到对应的评分策略");
        }
        // 根据应用类型和评分策略找到对应的评分策略
        ScoringStrategy scoringStrategy = scoringStrategyList.stream()
                .filter(strategy -> {
                    ScoringStrategyConfig annotation = strategy.getClass().getAnnotation(ScoringStrategyConfig.class);
                    return annotation.appType() == appType && annotation.scoringStrategy() == appScoringStrategy;
                })
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.SYSTEM_ERROR, "未找到对应的评分策略"));
        return scoringStrategy.doScore(choiceList, app);
    }
}
