package com.pnu.dev.pnufeedback.util;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.IntStream;

@Component
public class ScoreQuestionNumberComparator implements Comparator<String> {

    @Override
    public int compare(String questionNumber1, String questionNumber2) {

        String[] split1 = questionNumber1.split("\\.");
        String[] split2 = questionNumber2.split("\\.");

        return IntStream.range(0, Math.min(split1.length, split2.length))
                .map(idx -> Integer.compare(Integer.parseInt(split1[idx]), Integer.parseInt(split2[idx])))
                .filter(comparisonResult -> comparisonResult != 0)
                .findFirst()
                .orElse(Integer.compare(split1.length, split2.length));
    }

}
