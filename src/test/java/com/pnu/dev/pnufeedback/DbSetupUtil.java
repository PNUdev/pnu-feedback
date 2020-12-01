package com.pnu.dev.pnufeedback;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import com.pnu.dev.pnufeedback.repository.ScoreQuestionRepository;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class DbSetupUtil {

    @Autowired
    private EducationalProgramRepository educationalProgramRepository;

    @Autowired
    private StakeholderCategoryRepository stakeholderCategoryRepository;

    @Autowired
    private ScoreQuestionRepository scoreQuestionRepository;

    @Test
    void dbSetup() {

        List<EducationalProgram> educationalPrograms = IntStream.range(1, 5)
                .mapToObj(idx -> EducationalProgram.builder()
                        .title("Educational program " + idx)
                        .build())
                .collect(Collectors.toList());

        educationalProgramRepository.saveAll(educationalPrograms);

        List<StakeholderCategory> stakeholderCategories = IntStream.range(1, 5)
                .mapToObj(idx -> StakeholderCategory.builder()
                        .title("Stakeholder category " + idx)
                        .build())
                .collect(Collectors.toList());

        stakeholderCategoryRepository.saveAll(stakeholderCategories);

        List<ScoreQuestion> scoreQuestions = IntStream.range(1, 5)
                .mapToObj(idx -> IntStream.range(1, 5)
                        .mapToObj(iidx -> ScoreQuestion.builder()
                                .questionNumber("" + idx + "." + iidx)
                                .stakeholderCategoryId((long) idx)
                                .content("Question + " + idx + " + " + iidx)
                                .build())
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        scoreQuestionRepository.saveAll(scoreQuestions);

    }

}
