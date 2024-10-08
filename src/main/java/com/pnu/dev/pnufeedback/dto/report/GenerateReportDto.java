package com.pnu.dev.pnufeedback.dto.report;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateReportDto {

    private Long educationalProgramId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean includeStakeholderCategoriesWithZeroSubmissionsToPdfReport;

    private boolean showFullAnswers;

    private Map<String, String> chartColorMap;

}
