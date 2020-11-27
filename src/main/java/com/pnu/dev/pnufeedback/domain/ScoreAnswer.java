package com.pnu.dev.pnufeedback.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreAnswer {

    @Id
    private Long id;

    private Long educationalProgramId;

    private Long stakeholderCategoryId;

    private Long submissionId;

    private int score;

}
