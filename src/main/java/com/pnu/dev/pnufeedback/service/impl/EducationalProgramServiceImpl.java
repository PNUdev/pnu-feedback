package com.pnu.dev.pnufeedback.service.impl;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.dto.EducationalProgramDto;
import com.pnu.dev.pnufeedback.dto.form.EducationalProgramForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import com.pnu.dev.pnufeedback.service.EducationalProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EducationalProgramServiceImpl implements EducationalProgramService {

    private final EducationalProgramRepository repository;

    @Autowired
    public EducationalProgramServiceImpl(EducationalProgramRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EducationalProgramDto> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(educationalProgram -> EducationalProgramDto.builder()
                        .id(educationalProgram.getId())
                        .title(educationalProgram.getTitle())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public EducationalProgram findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceException("Educational program not found"));
    }

    @Override
    public void create(EducationalProgramForm educationalProgramForm) {
        if (repository.existsByTitle(educationalProgramForm.getTitle()))
            throw new ServiceException("Educational program already exists");

        EducationalProgram educationalProgram = EducationalProgram.builder()
                .title(educationalProgramForm.getTitle())
                .build();
        repository.save(educationalProgram);
    }

    @Override
    public void update(Long id, EducationalProgramForm educationalProgramForm) {
        EducationalProgram educationalProgramFromDb = findById(id);
        EducationalProgram updatedEducationalProgram = educationalProgramFromDb.toBuilder()
                .title(educationalProgramForm.getTitle())
                .build();
        repository.save(updatedEducationalProgram);
    }
}
