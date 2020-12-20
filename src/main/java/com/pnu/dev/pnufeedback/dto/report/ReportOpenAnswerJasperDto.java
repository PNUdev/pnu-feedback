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
public class ReportOpenAnswerJasperDto {

    private String stakeholderCategoryTitle;

    private List<ReportOpenAnswerContentJasperDto> openAnswerList;

}
