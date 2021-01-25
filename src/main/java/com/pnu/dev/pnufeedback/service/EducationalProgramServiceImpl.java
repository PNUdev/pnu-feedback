package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.dto.form.EducationalProgramForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import com.pnu.dev.pnufeedback.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationalProgramServiceImpl implements EducationalProgramService {

    private final static Sort SORT_BY_TITLE_ASC = Sort.by(Sort.Direction.ASC, "title");

    private final EducationalProgramRepository educationalProgramRepository;

    private final SubmissionRepository submissionRepository;

    @Autowired
    public EducationalProgramServiceImpl(EducationalProgramRepository educationalProgramRepository,
                                         SubmissionRepository submissionRepository) {

        this.educationalProgramRepository = educationalProgramRepository;
        this.submissionRepository = submissionRepository;
    }

    @Override
    public List<EducationalProgram> findAll() {
        return educationalProgramRepository.findAll(SORT_BY_TITLE_ASC);
    }

    @Override
    public List<EducationalProgram> findAllAllowedToBeSelectedByUser() {
        return educationalProgramRepository.findAllByAllowedToBeSelectedByUserTrue();
    }

    @Override
    public EducationalProgram findById(Long id) {
        return educationalProgramRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Освітня програма не знайдена"));
    }

    @Override
    public void create(EducationalProgramForm educationalProgramForm) {
        if (educationalProgramRepository.existsByTitle(educationalProgramForm.getTitle())) {
            throw new ServiceException("Освітня програма з такою назвою вже існує");
        }

        EducationalProgram educationalProgram = EducationalProgram.builder()
                .title(educationalProgramForm.getTitle())
                .allowedToBeSelectedByUser(educationalProgramForm.isAllowedToBeSelectedByUser())
                .build();
        educationalProgramRepository.save(educationalProgram);
    }

    @Override
    public void update(Long id, EducationalProgramForm educationalProgramForm) {
        if (educationalProgramRepository.existsByIdNotAndTitle(id, educationalProgramForm.getTitle())) {
            throw new ServiceException("Освітня програма з такою назвою вже існує");
        }

        EducationalProgram educationalProgramFromDb = findById(id);
        EducationalProgram updatedEducationalProgram = educationalProgramFromDb.toBuilder()
                .title(educationalProgramForm.getTitle())
                .allowedToBeSelectedByUser(educationalProgramForm.isAllowedToBeSelectedByUser())
                .build();

        educationalProgramRepository.save(updatedEducationalProgram);
    }

    @Override
    public void delete(Long id) {

        if (submissionRepository.existsByEducationalProgramId(id)) {
            throw new ServiceException("Неможливо видалити освітню програму," +
                    " оскільки вона вже була використана у опитуванні");
        }

        educationalProgramRepository.deleteById(id);

    }

    @Override
    public boolean existsByIdAndIsAllowedToBeSelectedByUser(Long id) {
        return educationalProgramRepository.existsByIdAndAllowedToBeSelectedByUserTrue(id);
    }

}
