package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.dto.ReportDataDto;
import com.pnu.dev.pnufeedback.dto.form.GenerateReportDto;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import com.pnu.dev.pnufeedback.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/generate-report")
public class ReportController {

    private EducationalProgramRepository educationalProgramRepository;
    private ReportService reportService;

    @Autowired
    public ReportController(EducationalProgramRepository educationalProgramRepository,
                            ReportService reportService) {
        this.educationalProgramRepository = educationalProgramRepository;
        this.reportService = reportService;
    }

    @GetMapping
    public String showGenerateReportPage(Model model) {

        List<EducationalProgram> educationalPrograms = educationalProgramRepository.findAll();
        model.addAttribute("educationalPrograms", educationalPrograms);

        return "admin/generate-report";
    }

    @PostMapping
    public void generateReport(GenerateReportDto generateReportDto, HttpServletResponse response) {
        log.info("Report generation started!");
        ReportDataDto reportDataDto = reportService.getReportData(generateReportDto);
        reportService.exportReport(reportDataDto, response);
        log.info("File successfully generated");
    }
}
