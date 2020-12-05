package com.pnu.dev.pnufeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackSubmissionDto {

    private List<ScoreAnswerDto> scoreAnswers;

    private String openAnswer;

    private Long educationalProgramId;

    private Long stakeholderCategoryId;

    private LocalDateTime submissionTime;

}
