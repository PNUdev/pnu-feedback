package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.report.ReportDataDto;
import com.pnu.dev.pnufeedback.dto.form.GenerateReportDto;

import javax.servlet.http.HttpServletResponse;

public interface ReportService {

    ReportDataDto getReportData(GenerateReportDto generateReportDto);

    void exportReport(ReportDataDto reportDataDto, HttpServletResponse response);
}
