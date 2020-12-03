package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScoreQuestionRepository extends CrudRepository<ScoreQuestion, Long> {

    List<ScoreQuestion> findAllByStakeholderCategoryId(Long stakeholderCategoryId);

    Page<ScoreQuestion> findAllByStakeholderCategoryId(Long id, Pageable pageable);

    boolean existsByStakeholderCategoryIdAndAndQuestionNumber(Long stakeholderCategoryId, String questionNumber);

    boolean existsByIdNotEqualsAndStakeholderCategoryIdAndAndQuestionNumber(Long id,
                                                                            Long stakeholderCategoryId,
                                                                            String questionNumber);
}
