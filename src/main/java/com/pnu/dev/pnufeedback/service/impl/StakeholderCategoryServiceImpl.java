package com.pnu.dev.pnufeedback.service.impl;

import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.form.StakeholderCategoryForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import com.pnu.dev.pnufeedback.service.StakeholderCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StakeholderCategoryServiceImpl implements StakeholderCategoryService {

    private final StakeholderCategoryRepository repository;

    @Autowired
    public StakeholderCategoryServiceImpl(StakeholderCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<StakeholderCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public StakeholderCategory findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceException("Stakeholder category not found"));
    }

    @Override
    public void create(StakeholderCategoryForm stakeholderCategoryForm) {
        if (repository.existsByTitle(stakeholderCategoryForm.getTitle()))
            throw new ServiceException("Educational program already exists");

        StakeholderCategory stakeholderCategory = StakeholderCategory.builder()
                .title(stakeholderCategoryForm.getTitle())
                .build();
        repository.save(stakeholderCategory);
    }

    @Override
    public void update(Long id, StakeholderCategoryForm stakeholderCategoryForm) {
        StakeholderCategory stakeholderCategoryFromDb = findById(id);
        StakeholderCategory updatedStakeholderCategory = stakeholderCategoryFromDb.toBuilder()
                .title(stakeholderCategoryForm.getTitle())
                .build();
        repository.save(updatedStakeholderCategory);
    }
}
