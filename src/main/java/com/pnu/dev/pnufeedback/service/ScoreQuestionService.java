package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import com.pnu.dev.pnufeedback.dto.ScoreQuestionDto;
import com.pnu.dev.pnufeedback.dto.form.ScoreQuestionForm;

import java.util.List;

public interface ScoreQuestionService {

    List<ScoreQuestionDto> findAllByStakeholderCategoryId(Long stakeHolderId);

    ScoreQuestionDto findById(Long id);

    ScoreQuestion create(ScoreQuestionForm scoreQuestionForm);

    ScoreQuestion update(Long id, ScoreQuestionForm scoreQuestionForm);

}
