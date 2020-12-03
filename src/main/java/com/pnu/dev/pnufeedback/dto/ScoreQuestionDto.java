package com.pnu.dev.pnufeedback.dto;

import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreQuestionDto {

    private Long id;

    private String questionNumber;

    private StakeholderCategory stakeholderCategory;

    private String content;
}
