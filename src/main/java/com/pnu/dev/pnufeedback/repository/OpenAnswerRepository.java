package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.OpenAnswer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OpenAnswerRepository extends PagingAndSortingRepository<OpenAnswer, Long> {

    List<OpenAnswer> findAllByReviewedFalse(Pageable pageable);

    long countAllByReviewedFalse();
}
