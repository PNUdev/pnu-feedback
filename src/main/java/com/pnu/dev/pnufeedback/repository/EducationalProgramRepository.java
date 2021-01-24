package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EducationalProgramRepository extends CrudRepository<EducationalProgram, Long> {

    List<EducationalProgram> findAll(Sort sort);

    List<EducationalProgram> findAllByAllowedToBeSelectedByUserTrue();

    boolean existsByTitle(String title);

    boolean existsByIdNotAndTitle(Long id, String title);

    boolean existsByIdAndAllowedToBeSelectedByUserTrue(Long id);

}
