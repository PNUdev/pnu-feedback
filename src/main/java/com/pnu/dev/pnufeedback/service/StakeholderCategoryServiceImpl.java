package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.form.StakeholderCategoryForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StakeholderCategoryServiceImpl implements StakeholderCategoryService {

    private final static Sort SORT_BY_TITLE_ASC = Sort.by(Sort.Direction.ASC, "title");

    private final StakeholderCategoryRepository repository;

    @Autowired
    public StakeholderCategoryServiceImpl(StakeholderCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<StakeholderCategory> findAll() {
        return repository.findAll(SORT_BY_TITLE_ASC);
    }

    @Override
    public StakeholderCategory findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceException("Категорія стейкхолдерів не знайдена"));
    }

    @Override
    public void create(StakeholderCategoryForm stakeholderCategoryForm) {
        if (repository.existsByTitle(stakeholderCategoryForm.getTitle())) {
            throw new ServiceException("Категорія стейкхолдерів з такою назвою вже існує");
        }

        StakeholderCategory stakeholderCategory = StakeholderCategory.builder()
                .title(stakeholderCategoryForm.getTitle())
                .build();
        repository.save(stakeholderCategory);
    }

    @Override
    public void update(Long id, StakeholderCategoryForm stakeholderCategoryForm) {
        if (repository.existsByIdNotAndTitle(id, stakeholderCategoryForm.getTitle())) {
            throw new ServiceException("Категорія стейкхолдерів з такою назвою вже існує");
        }

        StakeholderCategory stakeholderCategoryFromDb = findById(id);
        StakeholderCategory updatedStakeholderCategory = stakeholderCategoryFromDb.toBuilder()
                .title(stakeholderCategoryForm.getTitle())
                .build();
        repository.save(updatedStakeholderCategory);
    }
}
