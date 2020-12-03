package com.pnu.dev.pnufeedback.service.impl;

import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.ScoreQuestionDto;
import com.pnu.dev.pnufeedback.dto.form.ScoreQuestionForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.ScoreQuestionRepository;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import com.pnu.dev.pnufeedback.service.ScoreQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoreQuestionServiceImpl implements ScoreQuestionService {

    private final ScoreQuestionRepository scoreQuestionRepository;

    private final StakeholderCategoryRepository stakeholderCategoryRepository;

    @Autowired
    public ScoreQuestionServiceImpl(ScoreQuestionRepository scoreQuestionRepository,
                                    StakeholderCategoryRepository stakeholderCategoryRepository) {
        this.scoreQuestionRepository = scoreQuestionRepository;
        this.stakeholderCategoryRepository = stakeholderCategoryRepository;
    }


    @Override
    public Page<ScoreQuestionDto> findAllByStakeholderCategoryId(Long stakeHolderCategoryId, Pageable pageable) {

        StakeholderCategory stakeholderCategory = stakeholderCategoryRepository.findById(stakeHolderCategoryId)
                .orElseThrow(() -> new ServiceException("Stakeholder category not found"));

        Page<ScoreQuestion> scoreQuestionPage = scoreQuestionRepository
                .findAllByStakeholderCategoryId(stakeHolderCategoryId, pageable);

        List<ScoreQuestionDto> scoreQuestionDtos = scoreQuestionPage.getContent().stream()
                .map(scoreQuestion -> ScoreQuestionDto.builder()
                        .id(scoreQuestion.getId())
                        .questionNumber(scoreQuestion.getQuestionNumber())
                        .stakeholderCategory(stakeholderCategory)
                        .content(scoreQuestion.getContent())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(scoreQuestionDtos, scoreQuestionPage.getPageable(), scoreQuestionPage.getTotalElements());
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
    public void create(ScoreQuestionForm scoreQuestionForm) {

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

        scoreQuestionRepository.save(scoreQuestion);
    }

    @Override
    public void update(Long id, ScoreQuestionForm scoreQuestionForm) {

        ScoreQuestion scoreQuestionFromDb = scoreQuestionRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Питання не знайдено"));

        findStakeholderCategoryById(scoreQuestionForm.getStakeholderCategoryId());

        if (scoreQuestionRepository
                .existsByIdNotEqualsAndStakeholderCategoryIdAndAndQuestionNumber(
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

        scoreQuestionRepository.save(updatedScoreQuestion);
    }

    private StakeholderCategory findStakeholderCategoryById(Long stakeholderCategoryId) {
        return stakeholderCategoryRepository.findById(stakeholderCategoryId)
                .orElseThrow(() -> new ServiceException("Категорія стейкхолдерів не знайдена"));
    }
}
