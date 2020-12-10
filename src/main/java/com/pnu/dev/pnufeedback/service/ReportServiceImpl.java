package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.domain.ScoreAnswer;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.domain.Submission;
import com.pnu.dev.pnufeedback.dto.form.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.ReportAnswerInfoDto;
import com.pnu.dev.pnufeedback.dto.report.ReportDataDto;
import com.pnu.dev.pnufeedback.dto.report.ReportOpenAnswerDto;
import com.pnu.dev.pnufeedback.exception.EmptyReportException;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
import com.pnu.dev.pnufeedback.repository.OpenAnswerRepository;
import com.pnu.dev.pnufeedback.repository.ScoreAnswerRepository;
import com.pnu.dev.pnufeedback.repository.StakeholderCategoryRepository;
import com.pnu.dev.pnufeedback.repository.SubmissionRepository;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.summingInt;


@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private final static Integer CHART_SPLIT_SIZE = 45;
    private final static String TEMPLATE_PATH = "/reports/report-template.jrxml";
    private final static String REPORT_FILE = "/report.pdf";
    private final static DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toFormatter();

    private EducationalProgramRepository educationalProgramRepository;
    private ScoreAnswerRepository scoreAnswerRepository;
    private SubmissionRepository submissionRepository;
    private StakeholderCategoryRepository stakeholderCategoryRepository;
    private OpenAnswerRepository openAnswerRepository;

    public ReportServiceImpl(EducationalProgramRepository educationalProgramRepository,
                             ScoreAnswerRepository scoreAnswerRepository,
                             SubmissionRepository submissionRepository,
                             StakeholderCategoryRepository stakeholderCategoryRepository, OpenAnswerRepository openAnswerRepository) {
        this.educationalProgramRepository = educationalProgramRepository;
        this.scoreAnswerRepository = scoreAnswerRepository;
        this.submissionRepository = submissionRepository;
        this.stakeholderCategoryRepository = stakeholderCategoryRepository;
        this.openAnswerRepository = openAnswerRepository;
    }


    @Override
    public ReportDataDto getReportData(GenerateReportDto generateReportDto) {
        log.debug("Data analyzing has started!");

        LocalDate startDateTime = LocalDate.parse(generateReportDto.getStartDate(), DATE_TIME_FORMATTER);
        LocalDate endDateTime = LocalDate.parse(generateReportDto.getEndDate(), DATE_TIME_FORMATTER);

        EducationalProgram educationalProgram = educationalProgramRepository
                .findById(Long.valueOf(generateReportDto.getEducationalProgramId()))
                .orElseThrow(() -> new ServiceException("Educational program not found!"));
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
            ));
        }

        List<Long> submissionIds = submissions.stream().map(Submission::getId).collect(Collectors.toList());
        List<ScoreAnswer> scoreAnswers = scoreAnswerRepository.findAllBySubmissionIdIn(submissionIds);
        List<StakeholderCategory> stakeholderCategories = stakeholderCategoryRepository.findAll();


        List<ReportOpenAnswerDto> openAnswerData = openAnswerRepository.findAllBySubmissionIdsAndApproved(submissionIds);
        List<ReportAnswerInfoDto> answerData = getData(scoreAnswers, stakeholderCategories);
        String stakeholderStatistics = generateStakeHolderStatistics(answerData, stakeholderCategories.size());

        ReportDataDto reportDataDto = new ReportDataDto();
        reportDataDto.setStakeholderStatistics(stakeholderStatistics);
        reportDataDto.setEducationalProgramName(educationalProgram.getTitle());
        reportDataDto.setStartDate(startDateTime);
        reportDataDto.setEndDate(endDateTime);
        reportDataDto.setAnswerData(answerData);
        reportDataDto.setOpenAnswerData(openAnswerData);
        reportDataDto.setChartSplitSize(normalizeChartSplitSize(stakeholderCategories.size()));

        log.debug("All data gathered from: [{}] to: [{}]!", startDateTime, endDateTime);

        return reportDataDto;
    }

    @Override
    public void exportReport(ReportDataDto reportDataDto, HttpServletResponse response) {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + REPORT_FILE);

        try (ServletOutputStream servletOutputStream = response.getOutputStream()) {

            Resource resource = new ClassPathResource(TEMPLATE_PATH);
            JasperReport report = JasperCompileManager.compileReport(resource.getInputStream());

            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportDataDto.getAnswerData());
            Map<String, Object> parameters = prepareParameters(reportDataDto);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, beanColDataSource);

            JasperExportManager.exportReportToPdfStream(print, servletOutputStream);

        } catch (JRException | IOException e) {
            log.debug("Error occurred during report generation!", e.getLocalizedMessage());
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    private Map<String, Object> prepareParameters(ReportDataDto reportDataDto) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("EDUCATIONAL_PROGRAM_NAME", reportDataDto.getEducationalProgramName());
        parameters.put("START_DATE", reportDataDto.getStartDate().toString());
        parameters.put("END_DATE", reportDataDto.getEndDate().toString());
        parameters.put("STAKEHOLDER_STATISTICS", reportDataDto.getStakeholderStatistics());
        parameters.put("CHART_SPLIT_SIZE", reportDataDto.getChartSplitSize());
        parameters.put("OPEN_ANSWER_DATASET", reportDataDto.getOpenAnswerData());
        parameters.put("ANSWER_DATASET", new JRBeanCollectionDataSource(reportDataDto.getOpenAnswerData()));

        return parameters;
    }

    private List<ReportAnswerInfoDto> getData(
            List<ScoreAnswer> scoreAnswers,
            List<StakeholderCategory> stakeholderCategories
    ) {
        List<ReportAnswerInfoDto> answerInfos = new ArrayList<>();
        // Get all question numbers
        List<String> questionNumbers = getQuestionNumbers(scoreAnswers);
        // Get all stakeHolder numbers
        List<String> stakeHolderNumbers = getStakeholderNumbers(scoreAnswers);

        questionNumbers.stream().forEach(questionNumber -> {

            stakeHolderNumbers.stream().forEach(stakeHolderNumber -> {
                String question = stakeHolderNumber + "." + questionNumber;
                AtomicInteger count = new AtomicInteger(0);
                ReportAnswerInfoDto answerInfoDto = new ReportAnswerInfoDto();
                AtomicInteger questionScores = new AtomicInteger();

                scoreAnswers.stream()
                        .sorted(Comparator.comparing(ScoreAnswer::getQuestionNumber))
                        .filter(scoreAnswer -> scoreAnswer.getQuestionNumber().equals(question))
                        .forEach(scoreAnswer -> {
                                    //find stakeholder
                                    StakeholderCategory stakeholder = stakeholderCategories.stream()
                                            .filter(stakeholderCategory -> stakeholderCategory.getId().toString().equals(stakeHolderNumber))
                                            .findFirst().get();
                                    answerInfoDto.setStakeholderName(stakeholder.getTitle());
                                    answerInfoDto.setQuestion(mapQuestionNumber(questionNumber));
                                    answerInfoDto.setAnswerAmount(count.incrementAndGet());
                                    questionScores.addAndGet(scoreAnswer.getScore());
                                }
                        );

                if (!isNull(answerInfoDto.getAnswerAmount())) {
                    //find scores for one question from one stakeholder category
                    answerInfoDto.setScore(questionScores.doubleValue() / count.get());

                    //find answer info for one question
                    answerInfos.add(answerInfoDto);
                }
            });
        });

        return answerInfos.stream()
                .sorted(Comparator.comparing(ReportAnswerInfoDto::getQuestion))
                .collect(Collectors.toList());
    }

    private String mapQuestionNumber(String questionNumber) {
        Integer number = Integer.parseInt(questionNumber);
        Integer main = (number / 10) + 1;
        Integer remainder = (number % 10);

        return main + "." + remainder;
    }

    private String generateStakeHolderStatistics(List<ReportAnswerInfoDto> data, Integer stakeholderAmount){
        String statistics =  IntStream.range(0, stakeholderAmount)
                .mapToObj(i->data.get(i))
                .collect(
                        Collectors.groupingBy(
                                ReportAnswerInfoDto::getStakeholderName,
                                summingInt(ReportAnswerInfoDto::getAnswerAmount)
                        )
                ).toString();

        statistics = statistics.substring(1, statistics.length()-1);
        return statistics;
    }

    private List<String> getStakeholderNumbers(List<ScoreAnswer> scoreAnswers){
        return scoreAnswers.stream()
                .map(scoreAnswer -> scoreAnswer.getQuestionNumber())
                .map(questionNumber -> questionNumber.substring(0, questionNumber.indexOf(".")))
                .sorted()
                .distinct().collect(Collectors.toList());
    }

    private List<String> getQuestionNumbers(List<ScoreAnswer> scoreAnswers){
        return scoreAnswers.stream()
                .map(scoreAnswer -> scoreAnswer.getQuestionNumber())
                .map(questionNumber -> questionNumber.substring(questionNumber.indexOf(".") + 1))
                .sorted()
                .distinct()
                .collect(Collectors.toList());
    }

    private Integer normalizeChartSplitSize(Integer stakeholderAmount){
        if (stakeholderAmount < CHART_SPLIT_SIZE) {
            Integer remainder = CHART_SPLIT_SIZE % stakeholderAmount;
            return remainder == 0 ? CHART_SPLIT_SIZE : CHART_SPLIT_SIZE - remainder;
        }
        return stakeholderAmount;
    }


}
