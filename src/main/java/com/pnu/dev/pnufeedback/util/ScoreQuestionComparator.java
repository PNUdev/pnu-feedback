package com.pnu.dev.pnufeedback.util;

import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class ScoreQuestionComparator implements Comparator<ScoreQuestion> {

    private ScoreQuestionNumberComparator scoreQuestionNumberComparator;

    @Autowired
    public ScoreQuestionComparator(ScoreQuestionNumberComparator scoreQuestionNumberComparator) {
        this.scoreQuestionNumberComparator = scoreQuestionNumberComparator;
    }

    @Override
    public int compare(ScoreQuestion scoreQuestion1, ScoreQuestion scoreQuestion2) {
        String questionNumber1 = scoreQuestion1.getQuestionNumber();
        String questionNumber2 = scoreQuestion2.getQuestionNumber();
        return scoreQuestionNumberComparator.compare(questionNumber1, questionNumber2);
    }
}
