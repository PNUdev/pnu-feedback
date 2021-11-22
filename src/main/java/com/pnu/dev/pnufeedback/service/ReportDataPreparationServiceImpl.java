package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.domain.ScoreAnswer;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.domain.Submission;
import com.pnu.dev.pnufeedback.dto.report.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.ReportChartInfoJasperDto;
import com.pnu.dev.pnufeedback.dto.report.ReportOpenAnswerContentJasperDto;
import com.pnu.dev.pnufeedback.dto.report.ReportOpenAnswerDto;
import com.pnu.dev.pnufeedback.dto.report.ReportOpenAnswerJasperDto;
import com.pnu.dev.pnufeedback.dto.report.ScoreAnswerReportDataDto;
import com.pnu.dev.pnufeedback.exception.EmptyReportException;
import com.pnu.dev.pnufeedback.repository.OpenAnswerRepository;
import com.pnu.dev.pnufeedback.repository.ScoreAnswerRepository;
import com.pnu.dev.pnufeedback.repository.SubmissionRepository;
import com.pnu.dev.pnufeedback.util.ScoreQuestionNumberComparator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class ReportDataPreparationServiceImpl implements ReportDataPreparationService {

    private final static Integer CHART_SPLIT_SIZE = 25;

    private ScoreAnswerRepository scoreAnswerRepository;

    private SubmissionRepository submissionRepository;

    private OpenAnswerRepository openAnswerRepository;

    private EducationalProgramService educationalProgramService;

    private StakeholderCategoryService stakeholderCategoryService;

    private ScoreQuestionNumberComparator scoreQuestionNumberComparator;

    public ReportDataPreparationServiceImpl(ScoreAnswerRepository scoreAnswerRepository,
                                            SubmissionRepository submissionRepository,
                                            OpenAnswerRepository openAnswerRepository,
                                            EducationalProgramService educationalProgramService,
                                            StakeholderCategoryService stakeholderCategoryService,
                                            ScoreQuestionNumberComparator scoreQuestionNumberComparator) {

        this.scoreAnswerRepository = scoreAnswerRepository;
        this.submissionRepository = submissionRepository;
        this.openAnswerRepository = openAnswerRepository;
        this.educationalProgramService = educationalProgramService;
        this.stakeholderCategoryService = stakeholderCategoryService;
        this.scoreQuestionNumberComparator = scoreQuestionNumberComparator;
    }

    @Override
    public ScoreAnswerReportDataDto getReportData(GenerateReportDto generateReportDto) {

        log.debug("Data analyzing has started!");

        LocalDateTime startDate = generateReportDto.getStartDate();
        LocalDateTime endDate = generateReportDto.getEndDate();

        EducationalProgram educationalProgram = educationalProgramService
                .findById(generateReportDto.getEducationalProgramId());

        List<Submission> submissions = submissionRepository
                .findAllToShowInReportByEducationalProgramIdAndSubmissionTimeBetween(
                        educationalProgram.getId(), startDate, endDate
                );
        checkIfNotEmpty(submissions, startDate, endDate);

        List<StakeholderCategory> stakeholderCategories = stakeholderCategoryService.findAllToShowInReport();
        List<ReportOpenAnswerDto> openAnswerData = openAnswerRepository.findAllBySubmissionIdsAndApproved(
                getSubmissionIds(submissions)
        );
        List<ReportChartInfoJasperDto> chartAnswerData = getChartData(stakeholderCategories, submissions,
                generateReportDto.isIncludeStakeholderCategoriesWithZeroSubmissionsToPdfReport());
        checkIfNotEmpty(openAnswerData, startDate, endDate);

        String stakeholderStatistics = generateStakeHolderStatistics(chartAnswerData);

        ScoreAnswerReportDataDto reportDataDto = ScoreAnswerReportDataDto.builder()
                .stakeholderStatistics(stakeholderStatistics)
                .educationalProgramName(educationalProgram.getTitle())
                .startDate(startDate.toLocalDate())
                .endDate(endDate.toLocalDate())
                .scoreAnswerReportData(chartAnswerData)
                .openAnswerData(mapToJasperOpenAnswerDto(openAnswerData))
                .chartSplitSize(normalizeChartSplitSize(submissions)).build();

        log.debug("All data gathered from: [{}] to: [{}]!", startDate, endDate);

        return reportDataDto;
    }

    private List<ReportChartInfoJasperDto> getChartData(List<StakeholderCategory> stakeholderCategories,
                                                        List<Submission> submissions,
                                                        boolean includeStakeholderCategoriesWithZeroSubmissionsToPdfReport) {

        List<ScoreAnswer> scoreAnswers = scoreAnswerRepository.findAllBySubmissionIdIn(
                getSubmissionIds(submissions)
        );

        return scoreAnswers.stream()
                .map(ScoreAnswer::getQuestionNumber)
                .distinct()
                .sorted(scoreQuestionNumberComparator)
                .flatMap(questionNumber -> stakeholderCategories.stream()
                        .sorted(Comparator.comparing(StakeholderCategory::getId))
                        .map(stakeholderCategory -> {

                            List<Long> categoryStakeholderSubmissionIds = submissions.stream()
                                    .filter(submission -> submission.getStakeholderCategoryId()
                                            .equals(stakeholderCategory.getId()))
                                    .map(Submission::getId)
                                    .collect(toList());

                            MutableInt questionScoresSum = new MutableInt(0);

                            Long stakeholderAnswersCount = scoreAnswers.stream()
                                    .sorted(Comparator.comparing(ScoreAnswer::getQuestionNumber))
                                    .filter(scoreAnswer -> scoreAnswer.getQuestionNumber().equals(questionNumber))
                                    .filter(scoreAnswer -> categoryStakeholderSubmissionIds.contains(scoreAnswer.getSubmissionId()))
                                    .peek(scoreAnswer -> questionScoresSum.add(scoreAnswer.getScore()))
                                    .count();

                            return ReportChartInfoJasperDto.builder()
                                    .stakeholderCategoryTitle(stakeholderCategory.getTitle())
                                    .questionNumber(questionNumber)
                                    .averageScore(questionScoresSum.doubleValue() / stakeholderAnswersCount)
                                    .scoreAnswerCount(stakeholderAnswersCount.intValue()).build();

                        }))
                .filter(reportChartInfoJasperDto -> includeStakeholderCategoriesWithZeroSubmissionsToPdfReport
                        || reportChartInfoJasperDto.getScoreAnswerCount() != 0)
                .collect(Collectors.toList());

    }

    private String generateStakeHolderStatistics(List<ReportChartInfoJasperDto> data) {

        Map<String, Double> statisticsMap = data.stream()
                .collect(
                        Collectors.groupingBy(
                                ReportChartInfoJasperDto::getStakeholderCategoryTitle,
                                averagingInt(ReportChartInfoJasperDto::getScoreAnswerCount)
                        )
                );

        return statisticsMap.entrySet().stream()
                .map(e -> String.format("%s - %s", e.getKey(), e.getValue().intValue()))
                .collect(joining(", "));
    }


    private Integer normalizeChartSplitSize(List<Submission> submissions) {

        Integer stakeholderAmount = ((Long) submissions.stream()
                .map(Submission::getStakeholderCategoryId)
                .distinct()
                .count())
                .intValue();

        if (stakeholderAmount < CHART_SPLIT_SIZE) {

            Integer remainder = CHART_SPLIT_SIZE % stakeholderAmount;

            return remainder == 0 ? CHART_SPLIT_SIZE : CHART_SPLIT_SIZE - remainder;
        }

        return stakeholderAmount;
    }

    private List<ReportOpenAnswerJasperDto> mapToJasperOpenAnswerDto(List<ReportOpenAnswerDto> list) {

        return list.stream()
                .collect(
                        Collectors.groupingBy(
                                ReportOpenAnswerDto::getStakeholderCategoryTitle,
                                Collectors.mapping(
                                        item -> new ReportOpenAnswerContentJasperDto(item.getOpenAnswerContent()),
                                        toList()
                                )
                        )
                )
                .entrySet()
                .stream()
                .map(entry ->
                        new ReportOpenAnswerJasperDto(entry.getKey(), entry.getValue())
                )
                .collect(Collectors.toList());

    }

    private List<Long> getSubmissionIds(List<Submission> submissions) {
        return submissions.stream().map(Submission::getId).collect(Collectors.toList());
    }

    private <T> void checkIfNotEmpty(List<T> data, LocalDateTime startDate, LocalDateTime endDate) {
        if (data.isEmpty()) {
            throw new EmptyReportException(
                    String.format("У системі ще немає жодних результатів опитувань з %s по %s", startDate, endDate)
            );
        }
    }
}
