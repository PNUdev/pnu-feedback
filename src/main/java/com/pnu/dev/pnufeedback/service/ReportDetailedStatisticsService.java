package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.report.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.ReportDetailedStatistics;

public interface ReportDetailedStatisticsService {

    ReportDetailedStatistics calculateReportDetailedStatistics(GenerateReportDto generateReportDto);

}
