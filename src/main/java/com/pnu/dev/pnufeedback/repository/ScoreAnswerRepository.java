package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.ScoreAnswer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScoreAnswerRepository extends CrudRepository<ScoreAnswer, Long> {

    List<ScoreAnswer> findAllBySubmissionIdIn(List<Long> submissionIds);
}
