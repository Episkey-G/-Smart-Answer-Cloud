package com.episkey.SmartAnswerCloud.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.episkey.SmartAnswerCloud.model.dto.question.QuestionContentDTO;
import com.episkey.SmartAnswerCloud.model.entity.App;
import com.episkey.SmartAnswerCloud.model.entity.Question;
import com.episkey.SmartAnswerCloud.model.entity.ScoringResult;
import com.episkey.SmartAnswerCloud.model.entity.UserAnswer;
import com.episkey.SmartAnswerCloud.model.vo.QuestionVO;
import com.episkey.SmartAnswerCloud.service.QuestionService;
import com.episkey.SmartAnswerCloud.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/*
 * Copyright (c) 2024. All rights reserved.
 *
 * @author <a href="https://github.com/Episkey-G">Episkey</a>
 *
 * 自定义测试评分策略
 */
@ScoringStrategyConfig(appType = 0, scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;
    @Override
    public UserAnswer doScore(List<String> choice, App app) throws Exception {
        // 根据 id 查询到题目和题目结果信息（按分数降序排序）
        Long appId = app.getId();
        questionService.getOne(
                Wrappers.lambdaQuery(Question.class)
                        .eq(Question::getAppId, appId)
        );
        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, appId)
                        .orderByDesc(ScoringResult::getResultScoreRange)
        );

        // 统计用户总得分
        int totalScore = 0;
        QuestionVO questionVO = new QuestionVO();
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

        for (QuestionContentDTO questionContentDTO : questionContent) {
            for (String answer : choice) {
                for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                    if (option.getKey().equals(answer)) {
                        int score = Optional.of(option.getResult())
                                .map(Integer::parseInt)
                                .orElse(0);
                        totalScore += score;
                    }
                }
            }
        }

        // 遍历得分结果，找到第一个用户分数大于得分范围的结果，作为最终结果
        ScoringResult maxScoringResult = scoringResultList.get(0);

        for (ScoringResult scoringResult : scoringResultList) {
            if (totalScore >= scoringResult.getResultScoreRange()) {
                maxScoringResult = scoringResult;
                break;
            }
        }

        // 构造返回值，填充答案对象的属性
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choice));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        userAnswer.setResultScore(totalScore);

        return userAnswer;
    }
}
