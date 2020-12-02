package com.pnu.dev.pnufeedback.repository;

import com.pnu.dev.pnufeedback.domain.Submission;
import org.springframework.data.repository.CrudRepository;

public interface SubmissionRepository extends CrudRepository<Submission, Long> {

}
