package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.dto.form.GenerateReportDto;
import com.pnu.dev.pnufeedback.service.EducationalProgramService;
import com.pnu.dev.pnufeedback.service.ReportBuilderService;
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
    private EducationalProgramService educationalProgramService;

    @Autowired
    public ReportController(
            ReportBuilderService reportBuilderService,
            EducationalProgramService educationalProgramService) {
        this.reportBuilderService = reportBuilderService;
        this.educationalProgramService = educationalProgramService;
    }

    @GetMapping
    public String showGenerateReportPage(Model model) {

        List<EducationalProgram> educationalPrograms = educationalProgramService.findAll();
        model.addAttribute("educationalPrograms", educationalPrograms);

        return "admin/report/generate-report";
    }

    @PostMapping
    public void generateReport(@Validated GenerateReportDto generateReportDto, HttpServletResponse response) {

        log.info("Report generation started!");

        reportBuilderService.exportReport(generateReportDto, response);

        log.info("File successfully generated");
    }
}
