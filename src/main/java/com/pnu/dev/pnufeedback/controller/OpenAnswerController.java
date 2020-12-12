package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.OpenAnswer;
import com.pnu.dev.pnufeedback.service.OpenAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/open-answers")
public class OpenAnswerController {

    private final OpenAnswerService openAnswerService;

    @Autowired
    public OpenAnswerController(OpenAnswerService openAnswerService) {
        this.openAnswerService = openAnswerService;
    }

    @GetMapping
    public String findAllUnreviewed(Model model, @PageableDefault(size = 10, sort = "updatedAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OpenAnswer> openAnswersPage = openAnswerService.findAllByReviewed(false, pageable);
        model.addAttribute("openAnswersPage", openAnswersPage);
        model.addAttribute("reviewed", false);

        return "admin/openAnswer/index";
    }

    @GetMapping("/reviewed")
    public String findAllReviewed(Model model, @PageableDefault(size = 10, sort = "updatedAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OpenAnswer> openAnswersPage = openAnswerService.findAllByReviewed(true, pageable);
        model.addAttribute("openAnswersPage", openAnswersPage);
        model.addAttribute("reviewed", true);

        return "admin/openAnswer/index";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        openAnswerService.approve(id);

        return "redirect:/admin/open-answers";
    }

    @PostMapping("/disapprove/{id}")
    public String disapprove(@PathVariable Long id) {
        openAnswerService.disapprove(id);

        return "redirect:/admin/open-answers";
    }
}
