package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.form.StakeholderCategoryForm;

import java.util.List;

public interface StakeholderCategoryService {

    List<StakeholderCategory> findAll();

    List<StakeholderCategory> findAllToShowInReport();

    StakeholderCategory findById(Long id);

    void create(StakeholderCategoryForm educationalProgramForm);

    void update(Long id, StakeholderCategoryForm educationalProgramForm);

    void delete(Long id);

}
