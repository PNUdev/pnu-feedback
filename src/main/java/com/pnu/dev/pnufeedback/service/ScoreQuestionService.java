package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.ScoreQuestionDto;
import com.pnu.dev.pnufeedback.dto.form.ScoreQuestionForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScoreQuestionService {

    Page<ScoreQuestionDto> findAllByStakeholderCategoryId(Long stakeHolderId, Pageable pageable);

    ScoreQuestionDto findById(Long id);

    void create(ScoreQuestionForm scoreQuestionForm);

    void update(Long id, ScoreQuestionForm scoreQuestionForm);

}
