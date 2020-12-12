package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.OpenAnswer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OpenAnswerRepository extends CrudRepository<OpenAnswer, Long> {

    List<OpenAnswer> findAllByReviewed(boolean reviewed, Pageable pageable);

    List<OpenAnswer> findAllByReviewedAndApproved(boolean reviewed, boolean approved, Pageable pageable);

    long countAllByReviewed(boolean reviewed);

    long countAllByReviewedAndApproved(boolean reviewed, boolean approved);

}
