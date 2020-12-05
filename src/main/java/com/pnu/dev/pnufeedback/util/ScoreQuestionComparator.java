package com.pnu.dev.pnufeedback.util;

import com.pnu.dev.pnufeedback.dto.ScoreQuestionDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class ScoreQuestionComparator implements Comparator<ScoreQuestionDto> {

    @Override
    public int compare(ScoreQuestionDto o1, ScoreQuestionDto o2) {
        String questionNumber1=o1.getQuestionNumber();
        String questionNumber2=o2.getQuestionNumber();
        String[] split1 = questionNumber1.split("\\."), split2 = questionNumber2.split("\\.");
        int result = 0;
        for (int i = 0; i < Math.min(split1.length, split2.length); i++) {
            // compare current segment
            if ((result = Integer.compare(Integer.parseInt(split1[i]), Integer.parseInt(split2[i]))) != 0) {
                return result;
            }
        }
        // all was equal up to now, like "1.1" vs "1.1.1"
        return Integer.compare(split1.length, split2.length);
    }
}
