package com.pnu.dev.pnufeedback.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Submission {

    @Id
    private Long id;

    private Long educationalProgramId;

    private Long stakeholderCategoryId;

    private LocalDateTime submissionTime;

}
