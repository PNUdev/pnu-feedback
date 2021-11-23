package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.OpenAnswer;
import com.pnu.dev.pnufeedback.dto.report.ReportOpenAnswerDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OpenAnswerRepository extends CrudRepository<OpenAnswer, Long> {

    List<OpenAnswer> findAllByReviewed(boolean reviewed, Pageable pageable);

    List<OpenAnswer> findAllByReviewedAndApproved(boolean reviewed, boolean approved, Pageable pageable);

    long countAllByReviewed(boolean reviewed);

    long countAllByReviewedAndApproved(boolean reviewed, boolean approved);

    @Query("SELECT sc.title as stakeholder_category_title, oa.content as open_answer_content " +
            "FROM open_answer as oa " +
            "JOIN submission as s ON s.id IN (:submissionIds) " +
            "AND  s.id = oa.submission_id " +
            "AND oa.approved = true " +
            "JOIN stakeholder_category sc " +
            "ON s.stakeholder_category_id = sc.id")
    List<ReportOpenAnswerDto> findAllBySubmissionIdsAndApproved(List<Long> submissionIds);

}
