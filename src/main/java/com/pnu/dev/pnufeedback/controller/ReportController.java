package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.dto.form.GenerateReportDto;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/generate-report")
public class ReportController {

    private EducationalProgramRepository educationalProgramRepository;

    @Autowired
    public ReportController(EducationalProgramRepository educationalProgramRepository) {
        this.educationalProgramRepository = educationalProgramRepository;
    }

    @GetMapping
    public String showGenerateReportPage(Model model) {

        List<EducationalProgram> educationalPrograms = educationalProgramRepository.findAll();
        model.addAttribute("educationalPrograms", educationalPrograms);

        return "admin/generate-report";
    }

    @PostMapping
    public String generateReport(GenerateReportDto generateReportDto, Model model) {
        log.debug("Report generation!" + generateReportDto);

        return "admin/show-generated-report";
    }

    @PostMapping("/download")
    public String downloadReport(Model model) {
        log.debug("Report downloading!");

        return "admin/show-generated-report";
    }
}
