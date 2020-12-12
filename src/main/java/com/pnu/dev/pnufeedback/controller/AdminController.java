package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.repository.SubmissionRepository;
import com.pnu.dev.pnufeedback.service.OpenAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private SubmissionRepository submissionRepository;

    private OpenAnswerService openAnswerService;

    @Autowired
    public AdminController(SubmissionRepository submissionRepository, OpenAnswerService openAnswerService) {
        this.submissionRepository = submissionRepository;
        this.openAnswerService = openAnswerService;
    }

    @GetMapping
    public String index(Model model) {

        long submissionsCount = submissionRepository.count();
        long unreviewedOpenAnswersCount = openAnswerService.countByReviewed(true);
        model.addAttribute("submissionsCount", submissionsCount);
        model.addAttribute("unreviewedOpenAnswersCount", unreviewedOpenAnswersCount);
        return "admin/index";
    }

}
