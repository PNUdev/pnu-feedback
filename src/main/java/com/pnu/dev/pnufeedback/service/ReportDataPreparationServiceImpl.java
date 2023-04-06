package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.domain.ScoreAnswer;
import com.pnu.dev.pnufeedback.domain.ScoreQuestion;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class ReportDataPreparationServiceImpl implements ReportDataPreparationService {

    private static final Integer CHART_SPLIT_SIZE = 25;
    private static final int CHART_QUESTION_DENOMINATOR_MAXIMUM = 90;
    private static final int QUESTION_CUTOFF_PREVENT_DENOMINATOR = 45;

    private ScoreAnswerRepository scoreAnswerRepository;

    private SubmissionRepository submissionRepository;

    private OpenAnswerRepository openAnswerRepository;

    private EducationalProgramService educationalProgramService;

    private StakeholderCategoryService stakeholderCategoryService;

    private ScoreQuestionNumberComparator scoreQuestionNumberComparator;

    private ScoreQuestionServiceImpl scoreQuestionService;

    public ReportDataPreparationServiceImpl(ScoreAnswerRepository scoreAnswerRepository,
            SubmissionRepository submissionRepository,
            OpenAnswerRepository openAnswerRepository,
            EducationalProgramService educationalProgramService,
            StakeholderCategoryService stakeholderCategoryService,
            ScoreQuestionNumberComparator scoreQuestionNumberComparator,
            ScoreQuestionServiceImpl scoreQuestionService
    ) {

        this.scoreAnswerRepository = scoreAnswerRepository;
        this.submissionRepository = submissionRepository;
        this.openAnswerRepository = openAnswerRepository;
        this.educationalProgramService = educationalProgramService;
        this.stakeholderCategoryService = stakeholderCategoryService;
        this.scoreQuestionNumberComparator = scoreQuestionNumberComparator;
        this.scoreQuestionService = scoreQuestionService;
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
        checkIfNoSubmissions(submissions, startDate.toLocalDate(), endDate.toLocalDate());

        List<StakeholderCategory> stakeholderCategories = stakeholderCategoryService.findAllToShowInReport();
        List<ReportOpenAnswerDto> openAnswerData = openAnswerRepository.findAllBySubmissionIdsAndApproved(
                getSubmissionIds(submissions)
        );
        List<ReportChartInfoJasperDto> chartAnswerData = getChartData(
                stakeholderCategories,
                submissions,
                generateReportDto.isIncludeStakeholderCategoriesWithZeroSubmissionsToPdfReport(),
                generateReportDto.isShowFullAnswers()
        );
        checkIfNoActiveOpenAnswerNorAnswers(openAnswerData, chartAnswerData, startDate.toLocalDate(),
                endDate.toLocalDate());

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

    private List<ReportChartInfoJasperDto> getChartData(
            List<StakeholderCategory> stakeholderCategories,
            List<Submission> submissions,
            boolean includeStakeholderCategoriesWithZeroSubmissionsToPdfReport,
            boolean showFullAnswers
    ) {

        List<ScoreAnswer> scoreAnswers = scoreAnswerRepository.findAllBySubmissionIdIn(
                getSubmissionIds(submissions)
        );

        List<ScoreQuestion> scoreQuestions = stakeholderCategories.stream().map(
                stakeholderCategory -> scoreQuestionService.findAllByStakeholderCategoryId(stakeholderCategory.getId())
        ).flatMap(Collection::stream).collect(toList());

        List<String> questionsTexts = scoreQuestions.stream().map(ScoreQuestion::getContent).collect(toList());
        int newLineDenominator =
                Math.min(
                        questionsTexts.stream().map(String::length).max(Integer::compareTo).orElse(0) / 2,
                        CHART_QUESTION_DENOMINATOR_MAXIMUM
                );
        AtomicInteger cutoffQuestionIndex = new AtomicInteger(1);
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
                                    .filter(scoreAnswer -> categoryStakeholderSubmissionIds.contains(
                                            scoreAnswer.getSubmissionId()))
                                    .peek(scoreAnswer -> questionScoresSum.add(scoreAnswer.getScore()))
                                    .count();

                            boolean isCutoffQuestion =
                                    cutoffQuestionIndex.get() % CHART_SPLIT_SIZE == 0
                                            || cutoffQuestionIndex.get() == 1;
                            List<String> questionTexts = showFullAnswers
                                    ? scoreQuestions.stream()
                                        .filter(question -> isQuestionEligible(questionNumber, stakeholderCategory, question))
                                        .map(ScoreQuestion::getContent)
                                        .map(question -> insertNewLines(question, "\n", newLineDenominator, isCutoffQuestion))
                                        .collect(toList())
                                    : Collections.emptyList();
                            if (!questionTexts.isEmpty()) {
                                cutoffQuestionIndex.incrementAndGet();
                            }
                            return ReportChartInfoJasperDto.builder()
                                    .stakeholderCategoryTitle(stakeholderCategory.getTitle())
                                    .questionNumber(questionNumber)
                                    .questionTexts(questionTexts)
                                    .averageScore(questionScoresSum.doubleValue() / stakeholderAnswersCount)
                                    .scoreAnswerCount(stakeholderAnswersCount.intValue()).build();

                        }))
                .filter(reportChartInfoJasperDto -> includeStakeholderCategoriesWithZeroSubmissionsToPdfReport
                        || reportChartInfoJasperDto.getScoreAnswerCount() != 0)
                .collect(Collectors.toList());

    }

    private boolean isQuestionEligible(String questionNumber, StakeholderCategory stakeholderCategory, ScoreQuestion question) {
        return question.getQuestionNumber().equals(questionNumber)
                && question.getStakeholderCategoryId().equals(stakeholderCategory.getId());
    }

    public static String insertNewLines(String text, String insert, int period, boolean canBeCutoff) {
        String[] works = text.split(" ");
        StringBuilder line = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (String work : works) {
            if (line.length() + work.length() > (canBeCutoff ? QUESTION_CUTOFF_PREVENT_DENOMINATOR : period)) {
                result.append(line).append(insert);
                line = new StringBuilder();
            }
            line.append(work).append(" ");
        }
        result.append(line);

        return result.toString();
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

    private void checkIfNoSubmissions(List<Submission> data, LocalDate startDate, LocalDate endDate) {
        if (data.isEmpty()) {
            throw new EmptyReportException(
                    String.format("У системі ще немає жодних результатів опитувань з %s по %s", startDate, endDate)
            );
        }
    }

    private void checkIfNoActiveOpenAnswerNorAnswers(
            List<ReportOpenAnswerDto> openAnswers, List<ReportChartInfoJasperDto> answers,
            LocalDate startDate, LocalDate endDate) {
        if (answers.isEmpty() && openAnswers.isEmpty()) {
            throw new EmptyReportException(
                    String.format("У системі ще немає жодних результатів опитувань з %s по %s", startDate, endDate)
            );
        }
    }
}
