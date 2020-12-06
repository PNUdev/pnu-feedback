package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.dto.form.EducationalProgramForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationalProgramServiceImpl implements EducationalProgramService {

    private final static Sort SORT_BY_TITLE_ASC = Sort.by(Sort.Direction.ASC, "title");

    private final EducationalProgramRepository repository;

    @Autowired
    public EducationalProgramServiceImpl(EducationalProgramRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EducationalProgram> findAll() {
        return repository.findAll(SORT_BY_TITLE_ASC);
    }

    @Override
    public EducationalProgram findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceException("Освітня програма не знайдена"));
    }

    @Override
    public void create(EducationalProgramForm educationalProgramForm) {
        if (repository.existsByTitle(educationalProgramForm.getTitle())) {
            throw new ServiceException("Освітня програма з такою назвою вже існує");
        }

        EducationalProgram educationalProgram = EducationalProgram.builder()
                .title(educationalProgramForm.getTitle())
                .build();
        repository.save(educationalProgram);
    }

    @Override
    public void update(Long id, EducationalProgramForm educationalProgramForm) {
        if (repository.existsByIdNotAndTitle(id, educationalProgramForm.getTitle())) {
            throw new ServiceException("Освітня програма з такою назвою вже існує");
        }

        EducationalProgram educationalProgramFromDb = findById(id);
        EducationalProgram updatedEducationalProgram = educationalProgramFromDb.toBuilder()
                .title(educationalProgramForm.getTitle())
                .build();
        repository.save(updatedEducationalProgram);
    }

}
