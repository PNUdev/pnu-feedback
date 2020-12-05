package com.pnu.dev.pnufeedback.controller;


import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.ScoreQuestionDto;
import com.pnu.dev.pnufeedback.dto.form.ScoreQuestionForm;
import com.pnu.dev.pnufeedback.service.ScoreQuestionService;
import com.pnu.dev.pnufeedback.service.StakeholderCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/stakeholder-categories/{stakeholderCategoryId}/score-questions")
public class ScoreQuestionController {

    private final ScoreQuestionService scoreQuestionService;

    private final StakeholderCategoryService stakeholderCategoryService;

    @Autowired
    public ScoreQuestionController(ScoreQuestionService scoreQuestionService, StakeholderCategoryService stakeholderCategoryService) {
        this.scoreQuestionService = scoreQuestionService;
        this.stakeholderCategoryService = stakeholderCategoryService;
    }

    @GetMapping
    public String findAll(@PathVariable(name = "stakeholderCategoryId") Long stakeholderCategoryId,
                          Model model) {
        List<ScoreQuestionDto> scoreQuestions =
                scoreQuestionService.findAllByStakeholderCategoryId(stakeholderCategoryId);
        StakeholderCategory stakeholderCategory = stakeholderCategoryService.findById(stakeholderCategoryId);
        model.addAttribute("scoreQuestions", scoreQuestions);
        model.addAttribute("stakeholderCategory", stakeholderCategory);
        return "admin/scoreQuestion/index";
    }


    @GetMapping("/new")
    public String createForm(@PathVariable(name = "stakeholderCategoryId") Long stakeholderCategoryId,
                             Model model) {
        StakeholderCategory stakeholderCategory = stakeholderCategoryService.findById(stakeholderCategoryId);
        model.addAttribute("stakeholderCategory", stakeholderCategory);
        return "admin/scoreQuestion/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id,
                           @PathVariable(name = "stakeholderCategoryId") Long stakeholderCategoryId,
                           Model model) {
        ScoreQuestionDto scoreQuestion = scoreQuestionService.findById(id);
        StakeholderCategory stakeholderCategory = stakeholderCategoryService.findById(stakeholderCategoryId);
        model.addAttribute("scoreQuestion", scoreQuestion);
        model.addAttribute("stakeholderCategory", stakeholderCategory);

        return "admin/scoreQuestion/form";
    }

    @PostMapping("/new")
    public String create(@Validated ScoreQuestionForm scoreQuestionForm) {

        ScoreQuestion scoreQuestion = scoreQuestionService.create(scoreQuestionForm);
        return String.format("redirect:/admin/stakeholder-categories/%s/score-questions",
                scoreQuestion.getStakeholderCategoryId());
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Validated ScoreQuestionForm scoreQuestionForm) {

        ScoreQuestion scoreQuestion = scoreQuestionService.update(id, scoreQuestionForm);
        return String.format("redirect:/admin/stakeholder-categories/%s/score-questions",
                scoreQuestion.getStakeholderCategoryId());
    }
}
