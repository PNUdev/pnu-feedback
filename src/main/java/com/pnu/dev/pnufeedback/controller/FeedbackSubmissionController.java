package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import com.pnu.dev.pnufeedback.dto.FeedbackSubmissionDto;
import com.pnu.dev.pnufeedback.dto.JwtTokenPayload;
import com.pnu.dev.pnufeedback.dto.ScoreAnswerDto;
import com.pnu.dev.pnufeedback.repository.ScoreQuestionRepository;
import com.pnu.dev.pnufeedback.service.JwtTokenService;
import com.pnu.dev.pnufeedback.util.JwtTokenPayloadValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import static com.pnu.dev.pnufeedback.service.FeedbackSubmissionProcessorImpl.SUBMISSIONS_QUEUE_TOPIC;

@Controller
@RequestMapping("/feedback")
public class FeedbackSubmissionController {

    private static final String SHOW_AFTER_SUBMITTED_ATTRIBUTE = "show-after-submitted";

    private static final String SCORE_QUESTION_PARAM_PREFIX = "questionNumber-";

    private JwtTokenService jwtTokenService;

    private ScoreQuestionRepository scoreQuestionRepository;

    private JwtTokenPayloadValidator jwtTokenPayloadValidator;

    private JmsTemplate jmsTemplate;

    @Autowired
    public FeedbackSubmissionController(JwtTokenService jwtTokenService,
                                        ScoreQuestionRepository scoreQuestionRepository,
                                        JwtTokenPayloadValidator jwtTokenPayloadValidator,
                                        JmsTemplate jmsTemplate) {

        this.jwtTokenService = jwtTokenService;
        this.scoreQuestionRepository = scoreQuestionRepository;
        this.jwtTokenPayloadValidator = jwtTokenPayloadValidator;
        this.jmsTemplate = jmsTemplate;
    }

    @GetMapping
    public String showFeedbackQuestionsPage(@RequestParam("token") String jwtToken, Model model) {

        JwtTokenPayload jwtTokenPayload = jwtTokenService.resolveTokenPayload(jwtToken);
        jwtTokenPayloadValidator.validate(jwtTokenPayload);

        List<ScoreQuestion> scoreQuestions = scoreQuestionRepository
                .findAllByStakeholderCategoryId(jwtTokenPayload.getStakeholderCategoryId());

        model.addAttribute("scoreQuestions", scoreQuestions);
        return "submission/feedback-submission";
    }

    @GetMapping("/after-submit")
    public String showAfterSubmitPage(Model model) {

        if (model.containsAttribute(SHOW_AFTER_SUBMITTED_ATTRIBUTE)) {
            return "submission/after-submitted";
        }

        return "redirect:/";
    }

    @PostMapping
    public String submitFeedback(@RequestParam Map<String, String> parameterMap,
                                 @ModelAttribute("openAnswer") String openAnswer,
                                 @RequestParam("token") String jwtToken,
                                 RedirectAttributes redirectAttributes) {

        JwtTokenPayload jwtTokenPayload = jwtTokenService.resolveTokenPayload(jwtToken);
        jwtTokenPayloadValidator.validate(jwtTokenPayload);

        List<ScoreAnswerDto> scoreAnswers = fetchScoreAnswers(parameterMap);

        FeedbackSubmissionDto feedbackSubmission = FeedbackSubmissionDto.builder()
                .openAnswer(openAnswer)
                .scoreAnswers(scoreAnswers)
                .educationalProgramId(jwtTokenPayload.getEducationalProgramId())
                .stakeholderCategoryId(jwtTokenPayload.getStakeholderCategoryId())
                .submissionTime(LocalDateTime.now(ZoneId.of("Europe/Kiev")))
                .build();

        jmsTemplate.convertAndSend(SUBMISSIONS_QUEUE_TOPIC, feedbackSubmission);

        redirectAttributes.addFlashAttribute(SHOW_AFTER_SUBMITTED_ATTRIBUTE, true);
        return "redirect:/feedback/after-submit";
    }

    private List<ScoreAnswerDto> fetchScoreAnswers(Map<String, String> parameterMap) {
        return parameterMap.entrySet().stream()
                .filter(entry -> StringUtils.startsWith(entry.getKey(), SCORE_QUESTION_PARAM_PREFIX))
                .map(entry -> ScoreAnswerDto.builder()
                        .questionNumber(
                                StringUtils.replace(entry.getKey(), SCORE_QUESTION_PARAM_PREFIX, StringUtils.EMPTY))
                        .score(Integer.parseInt(entry.getValue()))
                        .build())
                .collect(Collectors.toList());
    }

}
