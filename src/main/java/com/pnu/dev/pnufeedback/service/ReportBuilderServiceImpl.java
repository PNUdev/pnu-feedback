package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.form.GenerateReportForm;
import com.pnu.dev.pnufeedback.dto.report.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.ScoreAnswerReportDataDto;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.util.GenerateReportDtoPreparer;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ReportBuilderServiceImpl implements ReportBuilderService {

    private final static String TEMPLATE_PATH = "/reports/report-template.jrxml";

    private final static String REPORT_FILE = "/report-[%s--%s].pdf";

    private ReportDataPreparationService reportDataPreparationService;

    private GenerateReportDtoPreparer generateReportDtoPreparer;

    public ReportBuilderServiceImpl(ReportDataPreparationService reportDataPreparationService,
                                    GenerateReportDtoPreparer generateReportDtoPreparer) {

        this.reportDataPreparationService = reportDataPreparationService;
        this.generateReportDtoPreparer = generateReportDtoPreparer;
    }


    @Override
    public void exportReport(GenerateReportForm generateReportForm, HttpServletResponse response) {

        GenerateReportDto generateReportDto = generateReportDtoPreparer.prepare(generateReportForm);
        boolean showFullAnswers = generateReportDto.isShowFullAnswers();
        ScoreAnswerReportDataDto scoreAnswerReportDataDto = reportDataPreparationService.getReportData(generateReportDto);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" +
                String.format(REPORT_FILE, generateReportForm.getStartDate(), generateReportForm.getEndDate()));

        try (ServletOutputStream servletOutputStream = response.getOutputStream()) {

            Resource resource = new ClassPathResource(TEMPLATE_PATH);
            JasperReport report = JasperCompileManager.compileReport(resource.getInputStream());

            JRBeanCollectionDataSource beanColDataSource =
                    new JRBeanCollectionDataSource(scoreAnswerReportDataDto.getScoreAnswerReportData());
            Map<String, Object> parameters = prepareParameters(scoreAnswerReportDataDto, showFullAnswers);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, beanColDataSource);

            JasperExportManager.exportReportToPdfStream(print, servletOutputStream);

        } catch (JRException | IOException e) {

            log.debug("Error occurred during report generation!", e.getLocalizedMessage());

            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    private Map<String, Object> prepareParameters(ScoreAnswerReportDataDto scoreAnswerReportDataDto,
                                                  boolean showFullAnswers) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("EDUCATIONAL_PROGRAM_NAME", scoreAnswerReportDataDto.getEducationalProgramName());
        parameters.put("START_DATE", scoreAnswerReportDataDto.getStartDate().toString());
        parameters.put("END_DATE", scoreAnswerReportDataDto.getEndDate().toString());
        parameters.put("STAKEHOLDER_STATISTICS", scoreAnswerReportDataDto.getStakeholderStatistics());
        parameters.put("CHART_SPLIT_SIZE", scoreAnswerReportDataDto.getChartSplitSize());
        parameters.put("OPEN_ANSWER_DATASET", new JRBeanCollectionDataSource(scoreAnswerReportDataDto.getOpenAnswerData()));
        parameters.put("COLOR_MAP", scoreAnswerReportDataDto.getChartColorMap());

        List<Map<String, Object>> uniqueQuestionNumbers = new ArrayList<>();
        List<Map<String, Object>> questionTextList = new ArrayList<>();

        if (showFullAnswers) {
            uniqueQuestionNumbers = scoreAnswerReportDataDto.getScoreAnswerReportData().stream()
                    .map(dto -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("questionNumber", dto.getQuestionNumber() + ", ");
                        return map;
                    })
                    .distinct()
                    .collect(Collectors.toList());
        }
        String newLastTest = uniqueQuestionNumbers.get(uniqueQuestionNumbers.size() - 1).get("questionNumber")
                .toString().replaceAll(", ", "");
        uniqueQuestionNumbers.get(uniqueQuestionNumbers.size() - 1).put("questionNumber", newLastTest);
        parameters.put("allQuestionNumbers", uniqueQuestionNumbers);
        Set<String> seenQuestionNumbers = new HashSet<>();

        if (showFullAnswers) {
            questionTextList = scoreAnswerReportDataDto.getScoreAnswerReportData().stream()
                    .flatMap(dto -> dto.getQuestionTexts().stream()
                            .map(text -> {
                                Map<String, Object> map = new HashMap<>();
                                String questionNumber = dto.getQuestionNumber();
                                if (seenQuestionNumbers.contains(questionNumber)) {
                                    questionNumber = "";
                                } else {
                                    seenQuestionNumbers.add(questionNumber);
                                }
                                map.put("questionNumber", questionNumber);
                                map.put("questionText", text);
                                return map;
                            }))
                    .collect(Collectors.toList());
        }
        parameters.put("allQuestionTexts", questionTextList);

        parameters.put("LIST_NAME", showFullAnswers ? "Список всіх питань" : " ");

        return parameters;
    }

}
