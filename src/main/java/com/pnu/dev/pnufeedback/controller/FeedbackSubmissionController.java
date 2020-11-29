package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import com.pnu.dev.pnufeedback.dto.JwtTokenPayload;
import com.pnu.dev.pnufeedback.dto.form.FeedbackSubmissionForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import com.pnu.dev.pnufeedback.repository.ScoreQuestionRepository;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import com.pnu.dev.pnufeedback.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/feedback")
public class FeedbackSubmissionController {

    private JwtTokenService jwtTokenService;

    private EducationalProgramRepository educationalProgramRepository;

    private StakeholderCategoryRepository stakeholderCategoryRepository;

    private ScoreQuestionRepository scoreQuestionRepository;

    @Autowired
    public FeedbackSubmissionController(JwtTokenService jwtTokenService,
                                        EducationalProgramRepository educationalProgramRepository,
                                        StakeholderCategoryRepository stakeholderCategoryRepository,
                                        ScoreQuestionRepository scoreQuestionRepository) {

        this.jwtTokenService = jwtTokenService;
        this.educationalProgramRepository = educationalProgramRepository;
        this.stakeholderCategoryRepository = stakeholderCategoryRepository;
        this.scoreQuestionRepository = scoreQuestionRepository;
    }

    @GetMapping
    public String showFeedbackQuestionsPage(@RequestParam("token") String jwtToken, Model model) {

        JwtTokenPayload jwtTokenPayload = jwtTokenService.resolveTokenPayload(jwtToken);

        if (!educationalProgramRepository.existsById(jwtTokenPayload.getEducationalProgramId())) {
            throw new ServiceException("Освітньої програми не існує!");
        }

        if (!stakeholderCategoryRepository.existsById(jwtTokenPayload.getStakeholderCategoryId())) {
            throw new ServiceException("Категорії стейкхолдерів не існує!");
        }

        List<ScoreQuestion> scoreQuestions = scoreQuestionRepository
                .findAllByStakeholderCategoryId(jwtTokenPayload.getStakeholderCategoryId());

        model.addAttribute("scoreQuestions", scoreQuestions);

        return "submission/feedback-submission";
    }

    @GetMapping("/after-submit")
    public String showAfterSubmitPage(@RequestParam("token") String jwtToken) {

        return "submission/after-submitted";
    }

    @PostMapping
    public String submitFeedback(FeedbackSubmissionForm submissionForm, @RequestParam("token") String jwtToken) {
        // ToDo add redirect attributes here

        return "redirect:/feedback/after-submit";
    }

}
