package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.report.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.QuestionDetailedStatistics;

import java.util.List;

public interface ReportDetailedStatisticsService {

    List<QuestionDetailedStatistics> calculateReportDetailedStatistics(GenerateReportDto generateReportDto);

}
