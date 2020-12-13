package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.dto.form.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.ReportDataDto;
import com.pnu.dev.pnufeedback.service.EducationalProgramService;
import com.pnu.dev.pnufeedback.service.ReportBuilderService;
import com.pnu.dev.pnufeedback.service.ReportDataPreparationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/generate-report")
public class ReportController {

    private ReportBuilderService reportBuilderService;
    private ReportDataPreparationService reportDataPreparationService;
    private EducationalProgramService educationalProgramService;

    @Autowired
    public ReportController(
            ReportBuilderService reportBuilderService,
            ReportDataPreparationService reportDataPreparationService,
            EducationalProgramService educationalProgramService) {
        this.reportBuilderService = reportBuilderService;
        this.reportDataPreparationService = reportDataPreparationService;
        this.educationalProgramService = educationalProgramService;
    }

    @GetMapping
    public String showGenerateReportPage(Model model) {

        List<EducationalProgram> educationalPrograms = educationalProgramService.findAll();
        model.addAttribute("educationalPrograms", educationalPrograms);

        return "admin/generate-report";
    }

    @PostMapping
    public void generateReport(@Validated GenerateReportDto generateReportDto, HttpServletResponse response) {

        log.info("Report generation started!");

        ReportDataDto reportDataDto = reportDataPreparationService.getReportData(generateReportDto);
        reportBuilderService.exportReport(reportDataDto, response);

        log.info("File successfully generated");
    }
}
