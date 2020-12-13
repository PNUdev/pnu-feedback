package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.report.ReportDataDto;

import javax.servlet.http.HttpServletResponse;

public interface ReportBuilderService {

    void exportReport(ReportDataDto reportDataDto, HttpServletResponse response);

}
