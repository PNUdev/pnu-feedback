package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.form.GenerateTokenForm;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import com.pnu.dev.pnufeedback.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/generate-token")
public class TokenGenerationController {

    private EducationalProgramRepository educationalProgramRepository;

    private StakeholderCategoryRepository stakeholderCategoryRepository;

    private JwtTokenService jwtTokenService;

    @Autowired
    public TokenGenerationController(EducationalProgramRepository educationalProgramRepository,
                                     StakeholderCategoryRepository stakeholderCategoryRepository,
                                     JwtTokenService jwtTokenService) {

        this.educationalProgramRepository = educationalProgramRepository;
        this.stakeholderCategoryRepository = stakeholderCategoryRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping
    public String showGenerateTokenPage(Model model) {

        List<EducationalProgram> educationalPrograms = educationalProgramRepository.findAll();
        model.addAttribute("educationalPrograms", educationalPrograms);

        List<StakeholderCategory> stakeholderCategories = stakeholderCategoryRepository.findAll();
        model.addAttribute("stakeholderCategories", stakeholderCategories);

        return "admin/generate-token";
    }

    @PostMapping
    public String generateToken(GenerateTokenForm generateTokenForm, Model model) {

        String jwtTokenLink = jwtTokenService.generateTokenLink(generateTokenForm);
        model.addAttribute("generatedTokenLink", jwtTokenLink);

        return "admin/show-generated-token";
    }

}
