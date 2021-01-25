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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/${app.adminPanelUrl}/educational-programs")
public class EducationalProgramController {

    private final EducationalProgramService educationalProgramService;

    private final String redirectUrl;

    @Autowired
    public EducationalProgramController(EducationalProgramService educationalProgramService,
                                        @Value("${app.adminPanelUrl}") String adminPanelUrl) {

        this.educationalProgramService = educationalProgramService;
        this.redirectUrl = String.format("redirect:/%s/educational-programs", adminPanelUrl);
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

    @GetMapping("/delete/{id}") // the {id} should be in the url
    public String deleteConfirmation(Model model,
                                     @RequestHeader(value = "referer", defaultValue = "/") String returnBackUrl) {

        model.addAttribute("message", "Ви точно хочете видалити освітню програму?");
        model.addAttribute("returnBackUrl", returnBackUrl);

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/new")
    public String create(@Validated EducationalProgramForm educationalProgramForm) {

        educationalProgramService.create(educationalProgramForm);

        return redirectUrl;
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Validated EducationalProgramForm educationalProgramForm) {

        educationalProgramService.update(id, educationalProgramForm);

        return redirectUrl;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {

        educationalProgramService.delete(id);

        return redirectUrl;
    }

}
