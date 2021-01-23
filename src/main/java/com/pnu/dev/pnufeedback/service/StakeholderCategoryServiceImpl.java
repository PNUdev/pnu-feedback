package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.form.StakeholderCategoryForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.ScoreQuestionRepository;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import com.pnu.dev.pnufeedback.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StakeholderCategoryServiceImpl implements StakeholderCategoryService {

    private final static Sort SORT_BY_TITLE_ASC = Sort.by(Sort.Direction.ASC, "title");

    private final StakeholderCategoryRepository stakeholderCategoryRepository;

    private final ScoreQuestionRepository scoreQuestionRepository;

    private final SubmissionRepository submissionRepository;

    @Autowired
    public StakeholderCategoryServiceImpl(StakeholderCategoryRepository stakeholderCategoryRepository,
                                          ScoreQuestionRepository scoreQuestionRepository,
                                          SubmissionRepository submissionRepository) {

        this.stakeholderCategoryRepository = stakeholderCategoryRepository;
        this.scoreQuestionRepository = scoreQuestionRepository;
        this.submissionRepository = submissionRepository;
    }

    @Override
    public List<StakeholderCategory> findAll() {
        return stakeholderCategoryRepository.findAll(SORT_BY_TITLE_ASC);
    }

    @Override
    public List<StakeholderCategory> findAllToShowInReport() {
        return stakeholderCategoryRepository.findAllByShowInReportTrue(SORT_BY_TITLE_ASC);
    }

    @Override
    public StakeholderCategory findById(Long id) {
        return stakeholderCategoryRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Категорія стейкхолдерів не знайдена"));
    }

    @Override
    public void create(StakeholderCategoryForm stakeholderCategoryForm) {
        if (stakeholderCategoryRepository.existsByTitle(stakeholderCategoryForm.getTitle())) {
            throw new ServiceException("Категорія стейкхолдерів з такою назвою вже існує");
        }

        StakeholderCategory stakeholderCategory = StakeholderCategory.builder()
                .title(stakeholderCategoryForm.getTitle())
                .showInReport(stakeholderCategoryForm.isShowInReport())
                .build();
        stakeholderCategoryRepository.save(stakeholderCategory);
    }

    @Override
    public void update(Long id, StakeholderCategoryForm stakeholderCategoryForm) {
        if (stakeholderCategoryRepository.existsByIdNotAndTitle(id, stakeholderCategoryForm.getTitle())) {
            throw new ServiceException("Категорія стейкхолдерів з такою назвою вже існує");
        }

        StakeholderCategory stakeholderCategoryFromDb = findById(id);
        StakeholderCategory updatedStakeholderCategory = stakeholderCategoryFromDb.toBuilder()
                .title(stakeholderCategoryForm.getTitle())
                .showInReport(stakeholderCategoryForm.isShowInReport())
                .build();
        stakeholderCategoryRepository.save(updatedStakeholderCategory);
    }

    @Override
    public void delete(Long id) {

        if (scoreQuestionRepository.existsByStakeholderCategoryId(id)) {
            throw new ServiceException("Неможливо видалити категорію стейкхолдерів, оскільки вона містить запитання");
        }

        if (submissionRepository.existsByStakeholderCategoryId(id)) {
            throw new ServiceException("Неможливо видалити категорію стейкхолдерів," +
                    " оскільки вона вже була використана у опитуванні");
        }

        stakeholderCategoryRepository.deleteById(id);

    }

}
