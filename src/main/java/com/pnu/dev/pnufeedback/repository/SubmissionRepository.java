package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.Submission;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {

    @Query("SELECT sb.id, sb.educational_program_id, sb.stakeholder_category_id, sb.submission_time FROM submission as sb " +
            "JOIN stakeholder_category as sc ON sb.stakeholder_category_id = sc.id " +
            "WHERE sc.show_in_report = true " +
            "AND sb.educational_program_id = :educationalProgramId " +
            "AND sb.submission_time BETWEEN :startTime AND :endTime")
    List<Submission> findAllToShowInReportByEducationalProgramIdAndSubmissionTimeBetween(
            @Param("educationalProgramId") Long educationalProgramId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    boolean existsByEducationalProgramId(Long educationalProgramId);

    boolean existsByStakeholderCategoryId(Long stakeholderCategoryId);

}
