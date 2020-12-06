package com.pnu.dev.pnufeedback.dto;

import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerInfoDto {
    private StakeholderCategory stakeholderCategory;
    private String stakeholderName;
    private String question;
    private Double score;
    private Integer answerAmount;
}
