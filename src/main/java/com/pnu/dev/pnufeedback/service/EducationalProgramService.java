package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.dto.form.EducationalProgramForm;

import java.util.List;

public interface EducationalProgramService {

    List<EducationalProgram> findAll();

    EducationalProgram findById(Long id);

    void create(EducationalProgramForm educationalProgramForm);

    void update(Long id, EducationalProgramForm educationalProgramForm);

    void delete(Long id);

    boolean existsById(Long id);

}
