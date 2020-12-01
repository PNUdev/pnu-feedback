package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import com.pnu.dev.pnufeedback.dto.FeedbackSubmissionDto;
import com.pnu.dev.pnufeedback.dto.JwtTokenPayload;
import com.pnu.dev.pnufeedback.dto.ScoreAnswerDto;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import com.pnu.dev.pnufeedback.repository.ScoreQuestionRepository;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import com.pnu.dev.pnufeedback.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// ToDo refactor

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
    public String showAfterSubmitPage(Model model) {

        if (model.containsAttribute("show-after-submitted")) {
            return "submission/after-submitted";
        }

        return "redirect:/";
    }

    @PostMapping
    public String submitFeedback(@RequestParam Map<String, String> map,
                                 @ModelAttribute("openAnswer") String openAnswer,
                                 @RequestParam("token") String jwtToken,
                                 RedirectAttributes redirectAttributes) {

        JwtTokenPayload jwtTokenPayload = jwtTokenService.resolveTokenPayload(jwtToken);

        List<ScoreAnswerDto> scoreAnswers = map.entrySet().stream()
                .filter(entry -> StringUtils.startsWithIgnoreCase(entry.getKey(), "questionNumber-"))
                .map(entry -> ScoreAnswerDto.builder()
                        .questionNumber(StringUtils.replace(entry.getKey(), "questionNumber-", ""))
                        .score(Integer.parseInt(entry.getValue()))
                        .build())
                .collect(Collectors.toList());

        FeedbackSubmissionDto feedbackSubmission = FeedbackSubmissionDto.builder()
                .openAnswer(openAnswer)
                .scoreAnswers(scoreAnswers)
                .educationalProgramId(jwtTokenPayload.getStakeholderCategoryId())
                .stakeholderCategoryId(jwtTokenPayload.getStakeholderCategoryId())
                .submissionTime(LocalDateTime.now(ZoneId.of("Europe/Kiev")))
                .build();

        redirectAttributes.addFlashAttribute("show-after-submitted", true);

        return "redirect:/feedback/after-submit";
    }

}
