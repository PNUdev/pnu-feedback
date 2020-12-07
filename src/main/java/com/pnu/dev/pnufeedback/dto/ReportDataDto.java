package com.pnu.dev.pnufeedback.dto;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDataDto {
    private EducationalProgram educationalProgram;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<AnswerInfoDto> data;
}
