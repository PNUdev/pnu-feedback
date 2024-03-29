package com.pnu.dev.pnufeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OpenAnswerDto {

    private Long id;

    private Long submissionId;

    private String content;

    private boolean approved;

    private boolean reviewed;

    private LocalDateTime updatedAt;

    private String educationalProgramTitle;

}