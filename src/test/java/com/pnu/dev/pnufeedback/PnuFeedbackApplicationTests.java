package com.pnu.dev.pnufeedback;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class PnuFeedbackApplicationTests {

    @Autowired
    private EducationalProgramRepository educationalProgramRepository;

    @Autowired
    private StakeholderCategoryRepository stakeholderCategoryRepository;

    @Test
    void dbSetup() {

        List<EducationalProgram> educationalPrograms = IntStream.range(0, 5)
                .mapToObj(idx -> EducationalProgram.builder()
                        .title("Educational program " + idx)
                        .build())
                .collect(Collectors.toList());

        educationalProgramRepository.saveAll(educationalPrograms);

        List<StakeholderCategory> stakeholderCategories = IntStream.range(0, 5)
                .mapToObj(idx -> StakeholderCategory.builder()
                        .title("Stakeholder category " + idx)
                        .build())
                .collect(Collectors.toList());

        stakeholderCategoryRepository.saveAll(stakeholderCategories);

    }

}
