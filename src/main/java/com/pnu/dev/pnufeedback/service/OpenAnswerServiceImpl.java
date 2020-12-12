package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.OpenAnswer;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.OpenAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OpenAnswerServiceImpl implements OpenAnswerService {

    private final OpenAnswerRepository openAnswerRepository;

    @Autowired
    public OpenAnswerServiceImpl(OpenAnswerRepository openAnswerRepository) {
        this.openAnswerRepository = openAnswerRepository;
    }

    @Override
    public Page<OpenAnswer> findAllForReview(Pageable pageable) {
        return new PageImpl(openAnswerRepository.findAllByReviewedFalse(pageable),
                pageable,
                countUnreviewed());
    }

    @Override
    public long countUnreviewed() {
        return openAnswerRepository.countAllByReviewedFalse();
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
                .build();
        openAnswerRepository.save(approvedOpenAnswer);
    }

    @Override
    public void disapprove(Long id) {
        OpenAnswer openAnswerFromDb = findById(id);

        OpenAnswer disapprovedOpenAnswer = openAnswerFromDb.toBuilder()
                .approved(false)
                .reviewed(true)
                .build();
        openAnswerRepository.save(disapprovedOpenAnswer);

    }

}
