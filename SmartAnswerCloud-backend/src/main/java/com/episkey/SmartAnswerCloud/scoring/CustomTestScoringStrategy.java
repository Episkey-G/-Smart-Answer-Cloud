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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Copyright (c) 2024. All rights reserved.
 *
 * @author <a href="https://github.com/Episkey-G">Episkey</a>
 *
 * 自定义测试评分策略
 */
@ScoringStrategyConfig(appType = 1, scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    /**
     * 执行评分
     *
     * @param choice 选择
     * @param app    应用
     * @return 评分结果
     * @throws Exception 异常
     */
    @Override
    public UserAnswer doScore(List<String> choice, App app) throws Exception {
        // 根据 id 查询到题目和题目结果信息
        Long appId = app.getId();
        questionService.getOne(
                Wrappers.lambdaQuery(Question.class)
                        .eq(Question::getAppId, appId)
        );
        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, appId)
        );
        // 统计用户每个选择对应的属性个数，如I = 10 个，E = 5 个
        QuestionVO questionVO = new QuestionVO();
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();
        Map<String, String> answerResultMap = new HashMap<>();
        for (QuestionContentDTO questionContentDTO : questionContent) {
            for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                for (String answer : choice) {
                    if (option.getKey().equals(answer)) {
                        answerResultMap.put(answer, option.getResult());
                    }
                }
            }
        }
        // 统计每个result的个数
        Map<String, Integer> resultCount = new HashMap<>();
        for (String result : answerResultMap.values()) {
            resultCount.put(result, resultCount.getOrDefault(result, 0)+1);
        }
        // 遍历每种评分结果，计算哪个结果的得分更高
        int maxScore = 0;
        ScoringResult maxScoringResult = scoringResultList.get(0);
        for (ScoringResult scoringResult : scoringResultList) {
            List<String> resultProp = JSONUtil.toList(scoringResult.getResultProp(), String.class);
            int score = resultProp.stream()
                    .mapToInt(result -> resultCount.getOrDefault(result, 0))
                    .sum();
            if (score > maxScore) {
                maxScore = score;
                maxScoringResult = scoringResult;
            }
        }
        // 返回评分结果
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choice));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        
        return userAnswer;
    }
}
