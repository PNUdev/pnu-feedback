package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.dto.form.EducationalProgramForm;
import com.pnu.dev.pnufeedback.service.EducationalProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/${app.adminPanelUrl}/educational-programs")
public class EducationalProgramController {

    private final EducationalProgramService educationalProgramService;

    @Value("${app.adminPanelUrl}")
    private String adminPanelUrl;

    @Autowired
    public EducationalProgramController(EducationalProgramService educationalProgramService) {
        this.educationalProgramService = educationalProgramService;
    }

    @GetMapping
    public String findAll(Model model) {
        List<EducationalProgram> educationalPrograms = educationalProgramService.findAll();
        model.addAttribute("educationalPrograms", educationalPrograms);
        return "admin/educationalProgram/index";
    }


    @GetMapping("/new")
    public String createForm() {
        return "admin/educationalProgram/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {

        EducationalProgram educationalProgram = educationalProgramService.findById(id);
        model.addAttribute("educationalProgram", educationalProgram);

        return "admin/educationalProgram/form";
    }

    @PostMapping("/new")
    public String create(@Validated EducationalProgramForm educationalProgramForm) {

        educationalProgramService.create(educationalProgramForm);

        return String.format("redirect:/%s/educational-programs", adminPanelUrl);
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Validated EducationalProgramForm educationalProgramForm) {

        educationalProgramService.update(id, educationalProgramForm);

        return String.format("redirect:/%s/educational-programs", adminPanelUrl);
    }
}
