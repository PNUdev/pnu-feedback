package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.form.GenerateReportForm;

import javax.servlet.http.HttpServletResponse;

public interface ExcelReportBuilderService {

    void exportReport(GenerateReportForm generateReportForm, HttpServletResponse response);

}
