package com.pnu.dev.pnufeedback.processor;

import com.pnu.dev.pnufeedback.domain.OpenAnswer;
import com.pnu.dev.pnufeedback.domain.ScoreAnswer;
import com.pnu.dev.pnufeedback.domain.Submission;
import com.pnu.dev.pnufeedback.dto.FeedbackSubmissionDto;
import com.pnu.dev.pnufeedback.repository.OpenAnswerRepository;
import com.pnu.dev.pnufeedback.repository.ScoreAnswerRepository;
import com.pnu.dev.pnufeedback.repository.SubmissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FeedbackSubmissionProcessorImpl implements FeedbackSubmissionProcessor {

    public static final String SUBMISSIONS_QUEUE_TOPIC = "submissions";

    private SubmissionRepository submissionRepository;

    private OpenAnswerRepository openAnswerRepository;

    private ScoreAnswerRepository scoreAnswerRepository;

    @Autowired
    public FeedbackSubmissionProcessorImpl(SubmissionRepository submissionRepository,
                                           OpenAnswerRepository openAnswerRepository,
                                           ScoreAnswerRepository scoreAnswerRepository) {

        this.submissionRepository = submissionRepository;
        this.openAnswerRepository = openAnswerRepository;
        this.scoreAnswerRepository = scoreAnswerRepository;
    }

    @Override
    @Transactional
    @JmsListener(destination = SUBMISSIONS_QUEUE_TOPIC, containerFactory = "submissionListenerFactory")
    public void processSubmission(FeedbackSubmissionDto feedbackSubmission) {

        log.info("Received submission: {}", feedbackSubmission);

        Submission submission = Submission.builder()
                .educationalProgramId(feedbackSubmission.getEducationalProgramId())
                .stakeholderCategoryId(feedbackSubmission.getStakeholderCategoryId())
                .submissionTime(feedbackSubmission.getSubmissionTime())
                .build();

        submissionRepository.save(submission);

        String openAnswerContent = feedbackSubmission.getOpenAnswer();
        if (StringUtils.isNotBlank(openAnswerContent)) {

            String sanitizedContent = StringEscapeUtils.escapeHtml4(openAnswerContent);

            OpenAnswer openAnswer = OpenAnswer.builder()
                    .submissionId(submission.getId())
                    .approved(false)
                    .content(sanitizedContent)
                    .build();

            openAnswerRepository.save(openAnswer);
        }

        List<ScoreAnswer> scoreAnswers = feedbackSubmission.getScoreAnswers().stream()
                .map(scoreAnswerDto -> ScoreAnswer.builder()
                        .questionNumber(scoreAnswerDto.getQuestionNumber())
                        .score(scoreAnswerDto.getScore())
                        .submissionId(submission.getId())
                        .build())
                .collect(Collectors.toList());

        scoreAnswerRepository.saveAll(scoreAnswers);

    }

}
