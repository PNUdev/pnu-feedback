package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EducationalProgramRepository extends CrudRepository<EducationalProgram, Long> {

    List<EducationalProgram> findAll();

    boolean existsByTitle(String title);

}
