package com.pnu.dev.pnufeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreAnswerDto {

    private String questionNumber;

    private Integer score;

}
