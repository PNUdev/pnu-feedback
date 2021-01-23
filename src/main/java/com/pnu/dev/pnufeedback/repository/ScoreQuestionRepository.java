package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScoreQuestionRepository extends CrudRepository<ScoreQuestion, Long> {

    List<ScoreQuestion> findAllByStakeholderCategoryId(Long stakeholderCategoryId);

    boolean existsByStakeholderCategoryIdAndAndQuestionNumber(Long stakeholderCategoryId, String questionNumber);

    boolean existsByIdNotAndStakeholderCategoryIdAndAndQuestionNumber(Long id,
                                                                      Long stakeholderCategoryId,
                                                                      String questionNumber);

    @Query("SELECT DISTINCT question_number FROM score_question")
    List<String> findAllAvailableQuestionNumbers();

    boolean existsByStakeholderCategoryId(Long stakeholderCategoryId);

}
