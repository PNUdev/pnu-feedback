package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.form.GenerateReportForm;
import com.pnu.dev.pnufeedback.service.EducationalProgramService;
import com.pnu.dev.pnufeedback.service.ExcelReportBuilderService;
import com.pnu.dev.pnufeedback.service.ReportBuilderService;
import com.pnu.dev.pnufeedback.service.StakeholderCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/${app.adminPanelUrl}/generate-report")
public class ReportController {

    private ReportBuilderService reportBuilderService;

    private ExcelReportBuilderService excelReportBuilderService;

    private EducationalProgramService educationalProgramService;

    private StakeholderCategoryService stakeholderCategoryService;

    @Autowired
    public ReportController(
        ReportBuilderService reportBuilderService,
        ExcelReportBuilderService excelReportBuilderService,
        EducationalProgramService educationalProgramService,
        StakeholderCategoryService stakeholderCategoryService) {

        this.reportBuilderService = reportBuilderService;
        this.excelReportBuilderService = excelReportBuilderService;
        this.educationalProgramService = educationalProgramService;
        this.stakeholderCategoryService = stakeholderCategoryService;
    }

    @GetMapping
    public String showGenerateReportPage(Model model) {

        List<EducationalProgram> educationalPrograms = educationalProgramService.findAll();
        List<StakeholderCategory> stakeholderCategories = stakeholderCategoryService.findAllToShowInReport();
        model.addAttribute("educationalPrograms", educationalPrograms);
        model.addAttribute("stakeholderCategories", stakeholderCategories);

        return "admin/report/generate-report";
    }

    @GetMapping("/pdf")
    public void generateReportPdf(@Validated GenerateReportForm generateReportForm,
                                  @RequestParam Map<String, String> colors, HttpServletResponse response) {

        log.info("PDF report generation started!");

        generateReportForm.setChartColorMap(colors);
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
