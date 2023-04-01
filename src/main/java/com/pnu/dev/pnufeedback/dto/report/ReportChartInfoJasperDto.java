package com.pnu.dev.pnufeedback.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportChartInfoJasperDto {

    private String stakeholderCategoryTitle;

    private String questionNumber;

    private List<String> questionTexts;

    private Double averageScore;

    private Integer scoreAnswerCount;

}
