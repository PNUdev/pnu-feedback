package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.form.GenerateReportDto;

import javax.servlet.http.HttpServletResponse;

public interface ReportBuilderService {

    void exportReport(GenerateReportDto generateReportDto, HttpServletResponse response);

}
