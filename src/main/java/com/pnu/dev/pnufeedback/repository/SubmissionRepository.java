package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.Submission;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {

    @Query("SELECT sb.id, sb.educational_program_id, sb.stakeholder_category_id, sb.submission_time FROM submission as sb " +
            "JOIN stakeholder_category as sc ON sb.stakeholder_category_id = sc.id and sc.show_in_report = true")
    List<Submission> findAllByEducationalProgramIdAndSubmissionTimeBetween(
            Long educationalProgramId, LocalDateTime startTime, LocalDateTime endTime);

    boolean existsByEducationalProgramId(Long educationalProgramId);

    boolean existsByStakeholderCategoryId(Long stakeholderCategoryId);

}
