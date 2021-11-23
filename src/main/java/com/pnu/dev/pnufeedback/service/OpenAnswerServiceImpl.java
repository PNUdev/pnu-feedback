package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.OpenAnswer;
import com.pnu.dev.pnufeedback.dto.OpenAnswerDto;
import com.pnu.dev.pnufeedback.dto.ReviewedFilter;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.OpenAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OpenAnswerServiceImpl implements OpenAnswerService {

    private final OpenAnswerRepository openAnswerRepository;

    @Autowired
    public OpenAnswerServiceImpl(OpenAnswerRepository openAnswerRepository) {
        this.openAnswerRepository = openAnswerRepository;
    }

    @Override
    public Page<OpenAnswerDto> findAllUnreviewed(Pageable pageable) {
        return new PageImpl(openAnswerRepository.findAllByReviewed(false, pageable.getPageSize(), pageable.getOffset()),
                pageable,
                countUnreviewed());
    }

    @Override
    public Page<OpenAnswerDto> findAllReviewed(ReviewedFilter reviewedFilter, Pageable pageable) {
        if (reviewedFilter == ReviewedFilter.APPROVED) {
            return new PageImpl(openAnswerRepository
                    .findAllByReviewedAndApproved(true, true, pageable.getPageSize(), pageable.getOffset()),
                    pageable,
                    openAnswerRepository.countAllByReviewedAndApproved(true, true));
        }
        if (reviewedFilter == ReviewedFilter.DISAPPROVED) {
            return new PageImpl(openAnswerRepository
                    .findAllByReviewedAndApproved(true, false, pageable.getPageSize(), pageable.getOffset()),
                    pageable,
                    openAnswerRepository.countAllByReviewedAndApproved(true, false));
        }
        return new PageImpl(openAnswerRepository.findAllByReviewed(true, pageable.getPageSize(), pageable.getOffset()),
                pageable,
                openAnswerRepository.countAllByReviewed(true));
    }

    @Override
    public long countUnreviewed() {
        return openAnswerRepository.countAllByReviewed(false);
    }

    @Override
    public OpenAnswer findById(Long id) {
        return openAnswerRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Відкрита відповідь не знайдена"));
    }

    @Override
    public void approve(Long id) {

        OpenAnswer openAnswerFromDb = findById(id);

        OpenAnswer approvedOpenAnswer = openAnswerFromDb.toBuilder()
                .approved(true)
                .reviewed(true)
                .updatedAt(LocalDateTime.now())
                .build();
        openAnswerRepository.save(approvedOpenAnswer);
    }

    @Override
    public void disapprove(Long id) {
        OpenAnswer openAnswerFromDb = findById(id);

        OpenAnswer disapprovedOpenAnswer = openAnswerFromDb.toBuilder()
                .approved(false)
                .reviewed(true)
                .updatedAt(LocalDateTime.now())
                .build();
        openAnswerRepository.save(disapprovedOpenAnswer);

    }

}
