package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.OpenAnswer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OpenAnswerRepository extends CrudRepository<OpenAnswer, Long> {

    List<OpenAnswer> findAllByReviewedFalse(Pageable pageable);

    long countAllByReviewedFalse();
}
