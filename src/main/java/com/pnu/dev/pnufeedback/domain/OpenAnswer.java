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
public class OpenAnswer {

    @Id
    private Long id;

    private Long educationalProgramId;

    private Long stakeholderCategoryId;

    private Long submissionId;

    private String content;

    private boolean approved;

}
