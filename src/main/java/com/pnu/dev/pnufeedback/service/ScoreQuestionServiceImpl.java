package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import com.pnu.dev.pnufeedback.dto.form.ScoreQuestionForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.ScoreQuestionRepository;
import com.pnu.dev.pnufeedback.util.ScoreQuestionComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class ScoreQuestionServiceImpl implements ScoreQuestionService {

    private final ScoreQuestionRepository scoreQuestionRepository;

    private final StakeholderCategoryService stakeholderCategoryService;

    private final ScoreQuestionComparator scoreQuestionComparator;

    @Autowired
    public ScoreQuestionServiceImpl(ScoreQuestionRepository scoreQuestionRepository,
                                    StakeholderCategoryService stakeholderCategoryService, ScoreQuestionComparator scoreQuestionComparator) {
        this.scoreQuestionRepository = scoreQuestionRepository;
        this.stakeholderCategoryService = stakeholderCategoryService;
        this.scoreQuestionComparator = scoreQuestionComparator;
    }


    @Override
    public List<ScoreQuestion> findAllByStakeholderCategoryId(Long stakeHolderCategoryId) {

        List<ScoreQuestion> scoreQuestions = scoreQuestionRepository
                .findAllByStakeholderCategoryId(stakeHolderCategoryId);
        scoreQuestions.sort(scoreQuestionComparator);

        return scoreQuestions;
    }

    @Override
    public ScoreQuestion findById(Long id) {

        return scoreQuestionRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Питання не знайдено"));
    }

    @Override
    public ScoreQuestion create(ScoreQuestionForm scoreQuestionForm) {

        stakeholderCategoryService.findById(scoreQuestionForm.getStakeholderCategoryId());

        if (isScoreQuestionNumberAvailable(null, scoreQuestionForm)) {
            throw new ServiceException("Питання з таким номером вже існує в даній категорії стейкхолдерів");
        }

        ScoreQuestion scoreQuestion = ScoreQuestion.builder()
                .questionNumber(scoreQuestionForm.getQuestionNumber())
                .stakeholderCategoryId(scoreQuestionForm.getStakeholderCategoryId())
                .content(scoreQuestionForm.getContent())
                .build();

        return scoreQuestionRepository.save(scoreQuestion);
    }

    @Override
    public ScoreQuestion update(Long id, ScoreQuestionForm scoreQuestionForm) {

        ScoreQuestion scoreQuestionFromDb = scoreQuestionRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Питання не знайдено"));

        stakeholderCategoryService.findById(scoreQuestionForm.getStakeholderCategoryId());

        if (isScoreQuestionNumberAvailable(id, scoreQuestionForm)) {
            throw new ServiceException("Питання з таким номером вже існує в даній категорії стейкхолдерів");
        }

        ScoreQuestion updatedScoreQuestion = ScoreQuestion.builder()
                .id(scoreQuestionFromDb.getId())
                .stakeholderCategoryId(scoreQuestionForm.getStakeholderCategoryId())
                .questionNumber(scoreQuestionForm.getQuestionNumber())
                .content(scoreQuestionForm.getContent())
                .build();

        return scoreQuestionRepository.save(updatedScoreQuestion);
    }

    private boolean isScoreQuestionNumberAvailable(@Nullable Long scoreQuestionId,
                                                   @NonNull ScoreQuestionForm scoreQuestionForm) {
        if (nonNull(scoreQuestionId)) {
            return scoreQuestionRepository.existsByIdNotAndStakeholderCategoryIdAndAndQuestionNumber(
                    scoreQuestionId,
                    scoreQuestionForm.getStakeholderCategoryId(),
                    scoreQuestionForm.getQuestionNumber()
            );
        } else {
            return scoreQuestionRepository.existsByStakeholderCategoryIdAndAndQuestionNumber(
                    scoreQuestionForm.getStakeholderCategoryId(),
                    scoreQuestionForm.getQuestionNumber()
            );
        }
    }
}