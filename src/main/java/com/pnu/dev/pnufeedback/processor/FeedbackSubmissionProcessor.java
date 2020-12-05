package com.pnu.dev.pnufeedback.processor;

import com.pnu.dev.pnufeedback.dto.FeedbackSubmissionDto;

public interface FeedbackSubmissionProcessor {

    void processSubmission(FeedbackSubmissionDto feedbackSubmission);

}
