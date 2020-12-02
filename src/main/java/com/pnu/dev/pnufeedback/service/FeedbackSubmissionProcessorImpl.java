package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.FeedbackSubmissionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FeedbackSubmissionProcessorImpl implements FeedbackSubmissionProcessor {

    @Override
    @JmsListener(destination = "submissions", containerFactory = "submissionListenerFactory")
    public void processSubmission(FeedbackSubmissionDto feedbackSubmission) {

        log.info("Received submission: {}", feedbackSubmission);

        System.out.println();

    }

}
