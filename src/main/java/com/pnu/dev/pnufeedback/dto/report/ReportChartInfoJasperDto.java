package com.pnu.dev.pnufeedback.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportChartInfoJasperDto {

    private String stakeholderCategoryTitle;

    private String questionNumber;

    private Double averageScore;

    private Integer scoreAnswerCount;

}
