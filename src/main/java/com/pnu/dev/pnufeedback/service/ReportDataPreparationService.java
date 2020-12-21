package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.report.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.ScoreAnswerReportDataDto;

public interface ReportDataPreparationService {

    ScoreAnswerReportDataDto getReportData(GenerateReportDto generateReportDto);

}
