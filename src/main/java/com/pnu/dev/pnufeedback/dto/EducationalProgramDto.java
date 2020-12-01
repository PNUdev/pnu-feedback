package com.pnu.dev.pnufeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationalProgramDto {

    private Long id;

    private String title;
}
