package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.dto.FeedbackSubmissionForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FeedbackSubmissionController {

    @GetMapping
    public String showFeedbackQuestionsPage(@RequestParam("token") String jwtToken) {


        return null;
    }

    @PostMapping
    public String submitFeedback(FeedbackSubmissionForm submissionForm, @RequestParam("token") String jwtToken) {


        return null;
    }

}
