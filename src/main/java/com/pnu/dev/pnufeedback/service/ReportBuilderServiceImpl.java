package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.form.GenerateReportForm;
import com.pnu.dev.pnufeedback.dto.report.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.ScoreAnswerReportDataDto;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.util.GenerateReportDtoPreparer;
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
import java.util.HashMap;
import java.util.Map;


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
        ScoreAnswerReportDataDto scoreAnswerReportDataDto = reportDataPreparationService.getReportData(generateReportDto);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" +
                String.format(REPORT_FILE, generateReportForm.getStartDate(), generateReportForm.getEndDate()));

        try (ServletOutputStream servletOutputStream = response.getOutputStream()) {

            Resource resource = new ClassPathResource(TEMPLATE_PATH);
            JasperReport report = JasperCompileManager.compileReport(resource.getInputStream());

            JRBeanCollectionDataSource beanColDataSource =
                    new JRBeanCollectionDataSource(scoreAnswerReportDataDto.getScoreAnswerReportData());
            Map<String, Object> parameters = prepareParameters(scoreAnswerReportDataDto);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, beanColDataSource);

            JasperExportManager.exportReportToPdfStream(print, servletOutputStream);

        } catch (JRException | IOException e) {

            log.debug("Error occurred during report generation!", e.getLocalizedMessage());

            throw new ServiceException(e.getLocalizedMessage());
        }
    }

    private Map<String, Object> prepareParameters(ScoreAnswerReportDataDto scoreAnswerReportDataDto) {

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("EDUCATIONAL_PROGRAM_NAME", scoreAnswerReportDataDto.getEducationalProgramName());
        parameters.put("START_DATE", scoreAnswerReportDataDto.getStartDate().toString());
        parameters.put("END_DATE", scoreAnswerReportDataDto.getEndDate().toString());
        parameters.put("STAKEHOLDER_STATISTICS", scoreAnswerReportDataDto.getStakeholderStatistics());
        parameters.put("CHART_SPLIT_SIZE", scoreAnswerReportDataDto.getChartSplitSize());
        parameters.put("OPEN_ANSWER_DATASET", new JRBeanCollectionDataSource(scoreAnswerReportDataDto.getOpenAnswerData()));
        parameters.put("COLOR_MAP", scoreAnswerReportDataDto.getChartColorMap());

        return parameters;
    }

}
