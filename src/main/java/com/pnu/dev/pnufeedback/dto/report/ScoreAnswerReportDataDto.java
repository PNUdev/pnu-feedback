package com.pnu.dev.pnufeedback.dto.report;

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
public class ScoreAnswerReportDataDto {

    private String educationalProgramName;

    private String stakeholderStatistics;

    private Integer chartSplitSize;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<ReportChartInfoJasperDto> scoreAnswerReportData;

    private List<ReportOpenAnswerJasperDto> openAnswerData;

}
