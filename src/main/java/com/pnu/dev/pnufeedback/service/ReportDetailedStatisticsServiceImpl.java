package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.ScoreAnswer;
import com.pnu.dev.pnufeedback.domain.Submission;
import com.pnu.dev.pnufeedback.dto.report.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.QuestionDetailedStatistics;
import com.pnu.dev.pnufeedback.dto.report.ReportDetailedStatistics;
import com.pnu.dev.pnufeedback.repository.ScoreAnswerRepository;
import com.pnu.dev.pnufeedback.repository.ScoreQuestionRepository;
import com.pnu.dev.pnufeedback.repository.SubmissionRepository;
import com.pnu.dev.pnufeedback.util.ScoreQuestionNumberComparator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
public class ReportDetailedStatisticsServiceImpl implements ReportDetailedStatisticsService {

    private SubmissionRepository submissionRepository;

    private ScoreAnswerRepository scoreAnswerRepository;

    private ScoreQuestionRepository scoreQuestionRepository;

    private ScoreQuestionNumberComparator scoreQuestionNumberComparator;

    @Autowired
    public ReportDetailedStatisticsServiceImpl(SubmissionRepository submissionRepository,
                                               ScoreAnswerRepository scoreAnswerRepository,
                                               ScoreQuestionRepository scoreQuestionRepository,
                                               ScoreQuestionNumberComparator scoreQuestionNumberComparator) {

        this.submissionRepository = submissionRepository;
        this.scoreAnswerRepository = scoreAnswerRepository;
        this.scoreQuestionRepository = scoreQuestionRepository;
        this.scoreQuestionNumberComparator = scoreQuestionNumberComparator;
    }

    @Override
    public ReportDetailedStatistics calculateReportDetailedStatistics(GenerateReportDto generateReportDto) {

        List<Submission> submissions = submissionRepository
                .findAllByEducationalProgramIdAndSubmissionTimeBetween(
                        generateReportDto.getEducationalProgramId(),
                        generateReportDto.getStartDate(),
                        generateReportDto.getEndDate()
                );

        List<Long> submissionIds = submissions.stream().map(Submission::getId).collect(Collectors.toList());
        List<ScoreAnswer> scoreAnswers = scoreAnswerRepository.findAllBySubmissionIdIn(submissionIds);

        Map<Long, Long> submissionsCountByStakeholderCategory = submissions.stream()
                .collect(groupingBy(Submission::getStakeholderCategoryId, counting()));

        List<String> questionNumbers = scoreQuestionRepository.findAllAvailableQuestionNumbers();

        List<QuestionDetailedStatistics> questionDetailedStatistics = questionNumbers.stream()
                .sorted(scoreQuestionNumberComparator)
                .map(questionNumber -> {

                    Map<Long, Double> averageScores = scoreAnswers.stream()
                            .filter(scoreAnswer -> StringUtils.equals(scoreAnswer.getQuestionNumber(), questionNumber))
                            .map(scoreAnswer -> ScoreAnswerStatisticsDto.builder()
                                    .questionNumber(scoreAnswer.getQuestionNumber())
                                    .score(scoreAnswer.getScore())
                                    .stakeholderCategoryId(
                                            findStakeholderCategoryIdByScoreAnswer(submissions, scoreAnswer))
                                    .build())
                            .collect(groupingBy(
                                    ScoreAnswerStatisticsDto::getStakeholderCategoryId,
                                    averagingInt(ScoreAnswerStatisticsDto::getScore)
                            ));

                    return QuestionDetailedStatistics.builder()
                            .questionNumber(questionNumber)
                            .averageScores(averageScores)
                            .build();
                })
                .collect(Collectors.toList());

        return ReportDetailedStatistics.builder()
                .submissionsCountByStakeholderCategory(submissionsCountByStakeholderCategory)
                .questionDetailedStatistics(questionDetailedStatistics)
                .build();
    }

    private Long findStakeholderCategoryIdByScoreAnswer(List<Submission> submissions, ScoreAnswer scoreAnswer) {
        return submissions.stream()
                .filter(submission -> submission.getId().equals(scoreAnswer.getSubmissionId()))
                .findFirst()
                .map(Submission::getStakeholderCategoryId)
                .get();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ScoreAnswerStatisticsDto {

        private String questionNumber;

        private Long stakeholderCategoryId;

        private int score;

    }

}
