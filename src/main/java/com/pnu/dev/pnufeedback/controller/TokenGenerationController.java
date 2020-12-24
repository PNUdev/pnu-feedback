package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.form.GenerateTokenForm;
import com.pnu.dev.pnufeedback.service.EducationalProgramService;
import com.pnu.dev.pnufeedback.service.JwtTokenService;
import com.pnu.dev.pnufeedback.service.StakeholderCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/${app.adminPanelUrl}/generate-token")
public class TokenGenerationController {

    private EducationalProgramService educationalProgramService;

    private StakeholderCategoryService stakeholderCategoryService;

    private JwtTokenService jwtTokenService;

    @Autowired
    public TokenGenerationController(EducationalProgramService educationalProgramService,
                                     StakeholderCategoryService stakeholderCategoryService,
                                     JwtTokenService jwtTokenService) {

        this.educationalProgramService = educationalProgramService;
        this.stakeholderCategoryService = stakeholderCategoryService;
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping
    public String showGenerateTokenPage(Model model) {

        List<EducationalProgram> educationalPrograms = educationalProgramService.findAll();
        model.addAttribute("educationalPrograms", educationalPrograms);

        List<StakeholderCategory> stakeholderCategories = stakeholderCategoryService.findAll();
        model.addAttribute("stakeholderCategories", stakeholderCategories);

        return "admin/token/generate-token";
    }

    @PostMapping
    public String generateToken(@Validated GenerateTokenForm generateTokenForm, Model model) {

        String jwtTokenLink = jwtTokenService.generateTokenLink(generateTokenForm);
        model.addAttribute("generatedTokenLink", jwtTokenLink);

        return "admin/token/show-generated-token";
    }

}
