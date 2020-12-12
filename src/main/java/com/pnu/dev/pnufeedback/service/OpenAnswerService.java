package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.OpenAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OpenAnswerService {

    Page<OpenAnswer> findAllByReviewed(boolean reviewed, Pageable pageable);

    long countByReviewed(boolean reviewed);

    OpenAnswer findById(Long id);

    void approve(Long id);

    void disapprove(Long id);
}
