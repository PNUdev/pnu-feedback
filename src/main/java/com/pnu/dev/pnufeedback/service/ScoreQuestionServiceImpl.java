package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import com.pnu.dev.pnufeedback.dto.form.ScoreQuestionForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.ScoreQuestionRepository;
import com.pnu.dev.pnufeedback.util.ScoreQuestionComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

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

        validateScoreQuestionNumber(scoreQuestionForm.getQuestionNumber());

        stakeholderCategoryService.findById(scoreQuestionForm.getStakeholderCategoryId());

        if (isScoreQuestionNumberAvailable(scoreQuestionForm)) {
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

        validateScoreQuestionNumber(scoreQuestionForm.getQuestionNumber());

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

    private boolean isScoreQuestionNumberAvailable(ScoreQuestionForm scoreQuestionForm) {
        return scoreQuestionRepository.existsByStakeholderCategoryIdAndAndQuestionNumber(
                scoreQuestionForm.getStakeholderCategoryId(),
                scoreQuestionForm.getQuestionNumber()
        );

    }

    private boolean isScoreQuestionNumberAvailable(Long scoreQuestionId, ScoreQuestionForm scoreQuestionForm) {
        return scoreQuestionRepository.existsByIdNotAndStakeholderCategoryIdAndAndQuestionNumber(
                scoreQuestionId,
                scoreQuestionForm.getStakeholderCategoryId(),
                scoreQuestionForm.getQuestionNumber()
        );
    }

    private void validateScoreQuestionNumber(String scoreQuestionNumber) {
        if (!Pattern.matches("^\\d+(\\.\\d+)*$", scoreQuestionNumber)) {
            throw new ServiceException("Номер запитання не відповіє патерну");
        }
    }
}
