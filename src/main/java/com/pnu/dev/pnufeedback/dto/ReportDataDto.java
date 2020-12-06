package com.pnu.dev.pnufeedback.dto;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDataDto {
    private EducationalProgram educationalProgram;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Map<String, List<AnswerInfoDto>> data;
}
