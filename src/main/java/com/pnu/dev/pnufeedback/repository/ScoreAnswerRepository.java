package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.ScoreAnswer;
import com.pnu.dev.pnufeedback.dto.report.ScoreAnswerStatisticsDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreAnswerRepository extends CrudRepository<ScoreAnswer, Long> {

    List<ScoreAnswer> findAllBySubmissionIdIn(List<Long> submissionIds);

    @Query("SELECT sa.question_number, sb.stakeholder_category_id, sa.score FROM score_answer as sa " +
            "JOIN submission as sb ON sa.submission_id = sb.id " +
            "WHERE sa.submission_id IN (:submissionIds)")
    List<ScoreAnswerStatisticsDto> findAllScoreAnswerStatisticsBySubmissionIdIn(
            @Param("submissionIds") List<Long> submissionIds);

}
