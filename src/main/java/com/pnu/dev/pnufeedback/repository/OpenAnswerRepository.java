package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.OpenAnswer;
import com.pnu.dev.pnufeedback.dto.OpenAnswerDto;
import com.pnu.dev.pnufeedback.dto.report.ReportOpenAnswerDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OpenAnswerRepository extends CrudRepository<OpenAnswer, Long> {

    @Query(
            "SELECT oa.*, ed.title as educational_program_title " +
                    "FROM open_answer as oa " +
                    "JOIN submission as s ON s.id = oa.submission_id " +
                    "AND oa.reviewed = :reviewed " +
                    "JOIN educational_program as ed " +
                    "ON s.educational_program_id = ed.id " +
                    "limit :limit offset :offset "
    )
    List<OpenAnswerDto> findAllByReviewed(boolean reviewed, Integer limit, Long offset);

    @Query(
            "SELECT oa.*, ed.title as educational_program_title " +
                    "FROM open_answer as oa " +
                    "JOIN submission as s ON s.id = oa.submission_id " +
                    "AND oa.reviewed = :reviewed " +
                    "AND oa.approved = :approved " +
                    "JOIN educational_program as ed " +
                    "ON s.educational_program_id = ed.id " +
                    "limit :limit offset :offset "
    )
    List<OpenAnswerDto> findAllByReviewedAndApproved(boolean reviewed, boolean approved, Integer limit, Long offset);

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
