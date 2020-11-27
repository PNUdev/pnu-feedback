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
public class ScoreQuestion {

    @Id
    private Long id;

    private String questionNumber;

    private Long stakeholderCategoryId;

    private String content;

}
