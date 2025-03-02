package com.pnu.dev.pnufeedback.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailedStatistics {

    private String questionNumber;

    private Map<Long, Double> averageScores; // stakeholderCategoryId -> averageScore

    public Double getDiscrepancyBetween(Long stakeholderCategoryId1, Long stakeholderCategoryId2) {
        Double score1 = averageScores.get(stakeholderCategoryId1);
        Double score2 = averageScores.get(stakeholderCategoryId2);

        if (score1 == null || score2 == null) {
            return null;
        }

        return Math.abs(score1 - score2);
    }

}
