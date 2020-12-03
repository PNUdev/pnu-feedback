package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StakeholderCategoryRepository extends CrudRepository<StakeholderCategory, Long> {

    List<StakeholderCategory> findAll();

    List<StakeholderCategory> findAllByIdIn(List<Long> idList);

    boolean existsByTitle(String title);
}
