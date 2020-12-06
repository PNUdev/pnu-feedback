package com.pnu.dev.pnufeedback.util;

import com.pnu.dev.pnufeedback.dto.ScoreQuestionDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class ScoreQuestionComparator implements Comparator<ScoreQuestionDto> {

    @Override
    public int compare(ScoreQuestionDto scoreQuestion1, ScoreQuestionDto scoreQuestion2) {
        String questionNumber1 = scoreQuestion1.getQuestionNumber();
        String questionNumber2 = scoreQuestion2.getQuestionNumber();
        String[] split1 = questionNumber1.split("\\.");
        String[] split2 = questionNumber2.split("\\.");
        int result = 0;
        for (int i = 0; i < Math.min(split1.length, split2.length); i++) {
            if ((result = Integer.compare(Integer.parseInt(split1[i]), Integer.parseInt(split2[i]))) != 0) {
                return result;
            }
        }
        return Integer.compare(split1.length, split2.length);
    }
}
