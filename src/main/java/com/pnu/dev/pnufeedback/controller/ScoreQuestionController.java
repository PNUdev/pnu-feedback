package com.pnu.dev.pnufeedback.controller;


import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.form.ScoreQuestionForm;
import com.pnu.dev.pnufeedback.service.ScoreQuestionService;
import com.pnu.dev.pnufeedback.service.StakeholderCategoryService;
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
@RequestMapping("/${app.adminPanelUrl}/stakeholder-categories/{stakeholderCategoryId}/score-questions")
public class ScoreQuestionController {

    private final ScoreQuestionService scoreQuestionService;

    private final StakeholderCategoryService stakeholderCategoryService;

    @Value("${app.adminPanelUrl}")
    private String adminPanelUrl;

    @Autowired
    public ScoreQuestionController(ScoreQuestionService scoreQuestionService,
                                   StakeholderCategoryService stakeholderCategoryService) {

        this.scoreQuestionService = scoreQuestionService;
        this.stakeholderCategoryService = stakeholderCategoryService;
    }

    @GetMapping
    public String findAll(@PathVariable(name = "stakeholderCategoryId") Long stakeholderCategoryId,
                          Model model) {
        List<ScoreQuestion> scoreQuestions =
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
        ScoreQuestion scoreQuestion = scoreQuestionService.findById(id);
        StakeholderCategory stakeholderCategory = stakeholderCategoryService.findById(stakeholderCategoryId);
        model.addAttribute("scoreQuestion", scoreQuestion);
        model.addAttribute("stakeholderCategory", stakeholderCategory);

        return "admin/scoreQuestion/form";
    }

    @GetMapping("/delete/{id}") // the {id} should be in the url
    public String deleteConfirmation(Model model,
                                     @RequestHeader(value = "referer", defaultValue = "/") String returnBackUrl) {

        model.addAttribute("message", "Ви точно хочете видалити запитання?");
        model.addAttribute("returnBackUrl", returnBackUrl);

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/new")
    public String create(@Validated ScoreQuestionForm scoreQuestionForm) {

        ScoreQuestion scoreQuestion = scoreQuestionService.create(scoreQuestionForm);
        return getRedirectUrl(scoreQuestion);
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Validated ScoreQuestionForm scoreQuestionForm) {

        ScoreQuestion scoreQuestion = scoreQuestionService.update(id, scoreQuestionForm);
        return getRedirectUrl(scoreQuestion);
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {

        ScoreQuestion scoreQuestion = scoreQuestionService.delete(id);
        return getRedirectUrl(scoreQuestion);
    }

    private String getRedirectUrl(ScoreQuestion scoreQuestion) {
        return String.format("redirect:/%s/stakeholder-categories/%s/score-questions",
                adminPanelUrl, scoreQuestion.getStakeholderCategoryId());
    }

}
