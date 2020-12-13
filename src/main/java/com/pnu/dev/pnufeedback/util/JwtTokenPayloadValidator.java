package com.pnu.dev.pnufeedback.util;

import com.pnu.dev.pnufeedback.dto.JwtTokenPayload;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenPayloadValidator {

    private EducationalProgramRepository educationalProgramRepository;

    private StakeholderCategoryRepository stakeholderCategoryRepository;

    @Autowired
    public JwtTokenPayloadValidator(EducationalProgramRepository educationalProgramRepository,
                                    StakeholderCategoryRepository stakeholderCategoryRepository) {

        this.educationalProgramRepository = educationalProgramRepository;
        this.stakeholderCategoryRepository = stakeholderCategoryRepository;
    }

    public void validate(JwtTokenPayload jwtTokenPayload) {

        if (!jwtTokenPayload.isAllowToChooseEducationalProgram() &&
                !educationalProgramRepository.existsById(jwtTokenPayload.getEducationalProgramId())) {

            throw new ServiceException("Освітню програму не знайдено!");
        }

        if (!stakeholderCategoryRepository.existsById(jwtTokenPayload.getStakeholderCategoryId())) {
            throw new ServiceException("Категорію стейкхолдерів не знайдено!");
        }
    }

}
