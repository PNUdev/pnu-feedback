package com.pnu.dev.pnufeedback.dto;

import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerInfoDto {
    private StakeholderCategory stakeholderCategory;
    private Integer answerAmount;
    private List<Integer> scores;
}
