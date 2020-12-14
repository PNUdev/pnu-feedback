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

    private String stakeholderName;

    private String question;

    private Double score;

    private Integer answerAmount;

}
