package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.domain.ScoreAnswer;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.domain.Submission;
import com.pnu.dev.pnufeedback.dto.form.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.ReportChartInfoJasperDto;
import com.pnu.dev.pnufeedback.dto.report.ReportDataDto;
import com.pnu.dev.pnufeedback.dto.report.ReportOpenAnswerContentJasperDto;
import com.pnu.dev.pnufeedback.dto.report.ReportOpenAnswerDto;
import com.pnu.dev.pnufeedback.dto.report.ReportOpenAnswerJasperDto;
import com.pnu.dev.pnufeedback.exception.EmptyReportException;
import com.pnu.dev.pnufeedback.repository.OpenAnswerRepository;
import com.pnu.dev.pnufeedback.repository.ScoreAnswerRepository;
import com.pnu.dev.pnufeedback.repository.SubmissionRepository;
import com.pnu.dev.pnufeedback.util.ScoreQuestionComparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class ReportDataPreparationServiceImpl implements ReportDataPreparationService {

    private final static Integer CHART_SPLIT_SIZE = 45;
    private final static DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toFormatter();

    private ScoreAnswerRepository scoreAnswerRepository;
    private SubmissionRepository submissionRepository;
    private OpenAnswerRepository openAnswerRepository;

    private EducationalProgramService educationalProgramService;
    private StakeholderCategoryService stakeholderCategoryService;

    private ScoreQuestionComparator scoreQuestionComparator;

    public ReportDataPreparationServiceImpl(ScoreAnswerRepository scoreAnswerRepository,
                                            SubmissionRepository submissionRepository,
                                            OpenAnswerRepository openAnswerRepository,
                                            EducationalProgramService educationalProgramService,
                                            StakeholderCategoryService stakeholderCategoryService,
                                            ScoreQuestionComparator scoreQuestionComparator) {
        this.scoreAnswerRepository = scoreAnswerRepository;
        this.submissionRepository = submissionRepository;
        this.openAnswerRepository = openAnswerRepository;
        this.educationalProgramService = educationalProgramService;
        this.stakeholderCategoryService = stakeholderCategoryService;
        this.scoreQuestionComparator = scoreQuestionComparator;
    }

    @Override
    public ReportDataDto getReportData(GenerateReportDto generateReportDto) {

        log.debug("Data analyzing has started!");

        LocalDate startDateTime = LocalDate.parse(generateReportDto.getStartDate(), DATE_TIME_FORMATTER);
        LocalDate endDateTime = LocalDate.parse(generateReportDto.getEndDate(), DATE_TIME_FORMATTER);

        EducationalProgram educationalProgram = educationalProgramService
                .findById(
                        Long.valueOf(generateReportDto.getEducationalProgramId())
                );
        List<Submission> submissions = submissionRepository
                .findAllByEducationalProgramIdAndSubmissionTimeBetween(
                        educationalProgram.getId(), startDateTime, endDateTime
                );

        if (submissions.isEmpty()) {
            throw new EmptyReportException(
                    String.format(
                            "У системі ще немає опитувань з %s по %s",
                            generateReportDto.getStartDate(),
                            generateReportDto.getEndDate()
                    )
            );
        }

        List<Long> submissionIds = submissions.stream().map(Submission::getId).collect(Collectors.toList());
        List<ScoreAnswer> scoreAnswers = scoreAnswerRepository.findAllBySubmissionIdIn(submissionIds);
        List<StakeholderCategory> stakeholderCategories = stakeholderCategoryService.findAll();

        List<ReportOpenAnswerDto> openAnswerData = openAnswerRepository.findAllBySubmissionIdsAndApproved(submissionIds);
        List<ReportChartInfoJasperDto> chartAnswerData = getChartData(scoreAnswers, stakeholderCategories, submissions);
        String stakeholderStatistics = generateStakeHolderStatistics(chartAnswerData);

        // Data filling up
        ReportDataDto reportDataDto = ReportDataDto.builder()
                .stakeholderStatistics(stakeholderStatistics)
                .educationalProgramName(educationalProgram.getTitle())
                .startDate(startDateTime)
                .endDate(endDateTime)
                .answerData(chartAnswerData)
                .openAnswerData(mapToJasperOpenAnswerDto(openAnswerData))
                .chartSplitSize(normalizeChartSplitSize(stakeholderCategories.size())).build();

        log.debug("All data gathered from: [{}] to: [{}]!", startDateTime, endDateTime);

        return reportDataDto;
    }

    private List<ReportChartInfoJasperDto> getChartData(List<ScoreAnswer> scoreAnswers,
                                                        List<StakeholderCategory> stakeholderCategories,
                                                        List<Submission> submissions) {



        return scoreAnswers.stream()
                .map(scoreAnswer -> scoreAnswer.getQuestionNumber())
                .sorted()
                .distinct()
                .flatMap(questionNumber -> stakeholderCategories.stream()
                        .sorted(Comparator.comparing(StakeholderCategory::getId))
                                .map(stakeholderCategory -> {

                                        AtomicInteger questionScores = new AtomicInteger(0);

                                        List<Long> categoryStakeholderSubmissionIds = submissions.stream()
                                                .filter(submission -> submission.getStakeholderCategoryId()
                                                        .equals(stakeholderCategory.getId())
                                                ).map(submission -> submission.getId()).collect(toList());

                                        Long stakeholderAnswerCount = scoreAnswers.stream()
                                                .sorted(Comparator.comparing(ScoreAnswer::getQuestionNumber))
                                                .filter(scoreAnswer -> scoreAnswer.getQuestionNumber().equals(questionNumber))
                                                .filter(scoreAnswer ->  categoryStakeholderSubmissionIds.contains(scoreAnswer.getSubmissionId()))
                                                .map(scoreAnswer -> questionScores.addAndGet(scoreAnswer.getScore()))
                                                .count();

                                        return ReportChartInfoJasperDto.builder()
                                                .stakeholderName(stakeholderCategory.getTitle())
                                                .question(questionNumber)
                                                .score(questionScores.doubleValue() / stakeholderAnswerCount)
                                                .answerAmount(stakeholderAnswerCount.intValue()).build();

                }))
                .filter(reportChartInfoJasperDto -> reportChartInfoJasperDto.getAnswerAmount() != 0)
                .sorted(Comparator.comparing(ReportChartInfoJasperDto::getQuestion))
                .collect(Collectors.toList());

    }

    private String generateStakeHolderStatistics(List<ReportChartInfoJasperDto> data) {

        Map<String, Double> statisticsMap = data.stream()
            .collect(
                 Collectors.groupingBy(
                      ReportChartInfoJasperDto::getStakeholderName,
                           averagingInt(ReportChartInfoJasperDto::getAnswerAmount)
                      )
            );

        String keyStatistics = statisticsMap.entrySet().stream()
                .map(e->e.getKey() + " - %s").collect(joining(", "));
        List<Integer> valueStatistics = statisticsMap.entrySet().stream()
                .map(e -> e.getValue().intValue()).collect(Collectors.toList());

        return String.format(keyStatistics, valueStatistics.toArray());
    }


    private Integer normalizeChartSplitSize(Integer stakeholderAmount) {

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
                                ReportOpenAnswerDto::getStakeholder,
                                Collectors.mapping(
                                        item -> new ReportOpenAnswerContentJasperDto(item.getContent()),
                                        toList()
                                )
                        )
                )
                .entrySet()
                .stream()
                .map(e ->
                        new ReportOpenAnswerJasperDto(e.getKey(), e.getValue())
                )
                .collect(Collectors.toList());

    }
}
