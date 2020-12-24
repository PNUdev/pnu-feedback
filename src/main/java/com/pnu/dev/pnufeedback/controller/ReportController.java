package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.dto.form.GenerateReportForm;
import com.pnu.dev.pnufeedback.service.EducationalProgramService;
import com.pnu.dev.pnufeedback.service.ExcelReportBuilderService;
import com.pnu.dev.pnufeedback.service.ReportBuilderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/${app.adminPanelUrl}/generate-report")
public class ReportController {

    private ReportBuilderService reportBuilderService;

    private ExcelReportBuilderService excelReportBuilderService;

    private EducationalProgramService educationalProgramService;

    @Autowired
    public ReportController(
            ReportBuilderService reportBuilderService,
            ExcelReportBuilderService excelReportBuilderService,
            EducationalProgramService educationalProgramService) {

        this.reportBuilderService = reportBuilderService;
        this.excelReportBuilderService = excelReportBuilderService;
        this.educationalProgramService = educationalProgramService;
    }

    @GetMapping
    public String showGenerateReportPage(Model model) {

        List<EducationalProgram> educationalPrograms = educationalProgramService.findAll();
        model.addAttribute("educationalPrograms", educationalPrograms);

        return "admin/report/generate-report";
    }

    @GetMapping("/pdf")
    public void generateReportPdf(@Validated GenerateReportForm generateReportForm, HttpServletResponse response) {

        log.info("PDF report generation started!");

        reportBuilderService.exportReport(generateReportForm, response);

        log.info("PDF report successfully generated");
    }

    @GetMapping("/excel")
    public void generateReportExcel(@Validated GenerateReportForm generateReportForm, HttpServletResponse response) {

        log.info("Excel report generation started!");

        excelReportBuilderService.exportReport(generateReportForm, response);

        log.info("Excel report successfully generated");
    }
}
