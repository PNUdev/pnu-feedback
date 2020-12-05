package com.pnu.dev.pnufeedback.service.impl;

import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.ScoreQuestionDto;
import com.pnu.dev.pnufeedback.dto.form.ScoreQuestionForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.ScoreQuestionRepository;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import com.pnu.dev.pnufeedback.service.ScoreQuestionService;
import com.pnu.dev.pnufeedback.util.ScoreQuestionComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoreQuestionServiceImpl implements ScoreQuestionService {

    private final ScoreQuestionRepository scoreQuestionRepository;

    private final StakeholderCategoryRepository stakeholderCategoryRepository;

    private final ScoreQuestionComparator scoreQuestionComparator;

    @Autowired
    public ScoreQuestionServiceImpl(ScoreQuestionRepository scoreQuestionRepository,
                                    StakeholderCategoryRepository stakeholderCategoryRepository, ScoreQuestionComparator scoreQuestionComparator) {
        this.scoreQuestionRepository = scoreQuestionRepository;
        this.stakeholderCategoryRepository = stakeholderCategoryRepository;
        this.scoreQuestionComparator = scoreQuestionComparator;
    }


    @Override
    public List<ScoreQuestionDto> findAllByStakeholderCategoryId(Long stakeHolderCategoryId) {

        StakeholderCategory stakeholderCategory = stakeholderCategoryRepository.findById(stakeHolderCategoryId)
                .orElseThrow(() -> new ServiceException("Stakeholder category not found"));

        List<ScoreQuestion> scoreQuestions = scoreQuestionRepository
                .findAllByStakeholderCategoryId(stakeHolderCategoryId);

        List<ScoreQuestionDto> scoreQuestionDtos = scoreQuestions.stream()
                .map(scoreQuestion -> ScoreQuestionDto.builder()
                        .id(scoreQuestion.getId())
                        .questionNumber(scoreQuestion.getQuestionNumber())
                        .stakeholderCategory(stakeholderCategory)
                        .content(scoreQuestion.getContent())
                        .build())
                .sorted(scoreQuestionComparator)
                .collect(Collectors.toList());

        return scoreQuestionDtos;
    }

    @Override
    public ScoreQuestionDto findById(Long id) {

        ScoreQuestion scoreQuestion = scoreQuestionRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Питання не знайдено"));

        StakeholderCategory stakeholderCategory = findStakeholderCategoryById(scoreQuestion.getStakeholderCategoryId());

        return ScoreQuestionDto.builder()
                .id(scoreQuestion.getId())
                .stakeholderCategory(stakeholderCategory)
                .questionNumber(scoreQuestion.getQuestionNumber())
                .content(scoreQuestion.getContent())
                .build();
    }

    @Override
    public ScoreQuestion create(ScoreQuestionForm scoreQuestionForm) {

        findStakeholderCategoryById(scoreQuestionForm.getStakeholderCategoryId());

        if (scoreQuestionRepository
                .existsByStakeholderCategoryIdAndAndQuestionNumber(scoreQuestionForm.getStakeholderCategoryId(),
                        scoreQuestionForm.getQuestionNumber())) {
            throw new ServiceException("Питання з таким номером вже існує в цій категорії стейкхолдерів");
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

        findStakeholderCategoryById(scoreQuestionForm.getStakeholderCategoryId());

        if (scoreQuestionRepository
                .existsByIdNotAndStakeholderCategoryIdAndAndQuestionNumber(
                        id,
                        scoreQuestionForm.getStakeholderCategoryId(),
                        scoreQuestionForm.getQuestionNumber())) {
            throw new ServiceException("Питання з таким номером вже існує в цій категорії стейкхолдерів");
        }

        ScoreQuestion updatedScoreQuestion = ScoreQuestion.builder()
                .id(scoreQuestionFromDb.getId())
                .stakeholderCategoryId(scoreQuestionForm.getStakeholderCategoryId())
                .questionNumber(scoreQuestionForm.getQuestionNumber())
                .content(scoreQuestionForm.getContent())
                .build();

        return scoreQuestionRepository.save(updatedScoreQuestion);
    }

    private StakeholderCategory findStakeholderCategoryById(Long stakeholderCategoryId) {
        return stakeholderCategoryRepository.findById(stakeholderCategoryId)
                .orElseThrow(() -> new ServiceException("Категорія стейкхолдерів не знайдена"));
    }
}
