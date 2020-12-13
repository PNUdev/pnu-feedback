package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.form.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.ReportDataDto;

public interface ReportDataPreparationService {

    ReportDataDto getReportData(GenerateReportDto generateReportDto);

}
