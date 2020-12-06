package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.domain.ScoreAnswer;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.domain.Submission;
import com.pnu.dev.pnufeedback.dto.Employee;
import com.pnu.dev.pnufeedback.dto.AnswerInfoDto;
import com.pnu.dev.pnufeedback.dto.ReportDataDto;
import com.pnu.dev.pnufeedback.dto.form.GenerateReportDto;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.repository.EducationalProgramRepository;
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
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;


@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private final static String TEMPLATE_PATH = "/reportTemplate.jrxml";
    private final static String REPORT_FILE= "/report.pdf";
    private final static DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    private EducationalProgramRepository educationalProgramRepository;
    private ScoreAnswerRepository scoreAnswerRepository;
    private SubmissionRepository submissionRepository;
    private StakeholderCategoryRepository stakeholderCategoryRepository;

    public ReportServiceImpl(EducationalProgramRepository educationalProgramRepository,
                             ScoreAnswerRepository scoreAnswerRepository,
                             SubmissionRepository submissionRepository,
                             StakeholderCategoryRepository stakeholderCategoryRepository) {
        this.educationalProgramRepository = educationalProgramRepository;
        this.scoreAnswerRepository = scoreAnswerRepository;
        this.submissionRepository = submissionRepository;
        this.stakeholderCategoryRepository = stakeholderCategoryRepository;
    }


    @Override
    public ReportDataDto getReportData(GenerateReportDto generateReportDto) {
        log.debug("Data analyzing has started!");

        LocalDateTime startDateTime = LocalDateTime.parse(generateReportDto.getStartDate(), DATE_TIME_FORMATTER);
        LocalDateTime endDateTime = LocalDateTime.parse(generateReportDto.getEndDate(), DATE_TIME_FORMATTER);

        EducationalProgram educationalProgram = educationalProgramRepository
                .findById(Long.valueOf(generateReportDto.getEducationalProgramId()))
                .orElseThrow(()-> new ServiceException("Educational program not found!"));
        List<Submission> submissions = submissionRepository
                .findAllByEducationalProgramIdAndSubmissionTimeBetween(
                        educationalProgram.getId(), startDateTime, endDateTime
                );

        Map<String, List<AnswerInfoDto>> data = getData(submissions);

        ReportDataDto reportDataDto = new ReportDataDto();
        reportDataDto.setEducationalProgram(educationalProgram);
        reportDataDto.setStartDate(startDateTime);
        reportDataDto.setEndDate(endDateTime);
        reportDataDto.setData(data);

        return reportDataDto;
    }

    @Override
    public void exportReport(ReportDataDto reportDataDto, HttpServletResponse response){

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + REPORT_FILE);

        try {
            final InputStream stream = this.getClass().getResourceAsStream(TEMPLATE_PATH);
            final JasperReport report = JasperCompileManager.compileReport(stream);
            final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(Arrays.asList(reportDataDto));

            final Map<String, Object> parameters = prepareParameters();
            parameters.put("CHART_DATASET", source);
            final JasperPrint print = JasperFillManager.fillReport(report, parameters);

            JasperExportManager.exportReportToPdfStream(print, response.getOutputStream());

        }catch (JRException| IOException e){
            log.debug("Error occurred during report generation!", e.getLocalizedMessage());
            throw new ServiceException(e.getLocalizedMessage());
        }finally {
            try {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }catch (IOException e) {
                log.debug("Error occurred during stream closing!", e.getLocalizedMessage());
                throw new ServiceException(e.getLocalizedMessage());
            }
        }
    }


    private Map<String, Object> prepareParameters(){
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "mslob.com");
        return parameters;
    }

    private Map<String, List<AnswerInfoDto>> getData(List<Submission> submissions){
        Map<String, List<AnswerInfoDto>> data = new HashMap<>();

        List<Long> submissionIds = submissions.stream().map(Submission::getId).collect(Collectors.toList());
        List<ScoreAnswer> scoreAnswers = scoreAnswerRepository.findAllBySubmissionIdIn(submissionIds);
        List<StakeholderCategory> stakeholderCategories = stakeholderCategoryRepository.findAll();

        //get all question numbers
        List<String> questionNumbers = scoreAnswers.stream()
                .map(scoreAnswer -> scoreAnswer.getQuestionNumber())
                .map(questionNumber-> questionNumber.substring(questionNumber.indexOf(".") + 1))
                .sorted()
                .distinct()
                .collect(Collectors.toList());
        //get all stakeHolder numbers
        List<String> stakeHolderNumbers = scoreAnswers.stream()
                .map(scoreAnswer -> scoreAnswer.getQuestionNumber())
                .map(questionNumber-> questionNumber.substring(0, questionNumber.indexOf(".")))
                .sorted()
                .distinct().collect(Collectors.toList());


        questionNumbers.stream().forEach(questionNumber->{

            List<AnswerInfoDto> answerInfos = new ArrayList<>();

            stakeHolderNumbers.stream().forEach(stakeHolderNumber->{
                String question = stakeHolderNumber + "." + questionNumber;
                AtomicInteger count = new AtomicInteger(0);
                AnswerInfoDto answerInfoDto = new AnswerInfoDto();
                List<Integer> questionScores = new ArrayList<>();

                scoreAnswers.stream()
                        .sorted(Comparator.comparing(ScoreAnswer::getQuestionNumber))
                        .filter(scoreAnswer -> scoreAnswer.getQuestionNumber().equals(question))
                        .forEach(scoreAnswer -> {
                            //find stakeholder
                            StakeholderCategory stakeholder = stakeholderCategories.stream()
                                    .filter(stakeholderCategory -> stakeholderCategory.getId().toString().equals(stakeHolderNumber))
                                    .findFirst().get();
                            answerInfoDto.setStakeholderCategory(stakeholder);
                            answerInfoDto.setAnswerAmount(count.incrementAndGet());
                            questionScores.add(scoreAnswer.getScore());
                        }
                );

                //find scores for one question from one stakeholder category
                answerInfoDto.setScores(questionScores);
                if (!isNull(answerInfoDto.getAnswerAmount())) {
                    //find answer info for one question
                    answerInfos.add(answerInfoDto);
                }
            });

            //prepare data
            data.put(questionNumber, answerInfos);
        });

        return data;
    }
}
