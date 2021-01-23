package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.Submission;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {

    List<Submission> findAllByEducationalProgramIdAndSubmissionTimeBetween(
            Long educationalProgramId, LocalDate startDate, LocalDate endDate);

    boolean existsByEducationalProgramId(Long educationalProgramId);

    boolean existsByStakeholderCategoryId(Long stakeholderCategoryId);

}
