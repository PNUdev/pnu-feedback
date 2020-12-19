package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.domain.EducationalProgram;
import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.form.GenerateReportForm;
import com.pnu.dev.pnufeedback.dto.report.GenerateReportDto;
import com.pnu.dev.pnufeedback.dto.report.QuestionDetailedStatistics;
import com.pnu.dev.pnufeedback.dto.report.ReportDetailedStatistics;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import com.pnu.dev.pnufeedback.util.GenerateReportDtoPreparer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Service
public class ExcelReportBuilderServiceImpl implements ExcelReportBuilderService {

    private ReportDetailedStatisticsService reportDetailedStatisticsService;

    private StakeholderCategoryService stakeholderCategoryService;

    private EducationalProgramService educationalProgramService;

    private GenerateReportDtoPreparer generateReportDtoPreparer;

    @Autowired
    public ExcelReportBuilderServiceImpl(ReportDetailedStatisticsService reportDetailedStatisticsService,
                                         StakeholderCategoryService stakeholderCategoryService,
                                         EducationalProgramService educationalProgramService,
                                         GenerateReportDtoPreparer generateReportDtoPreparer) {

        this.reportDetailedStatisticsService = reportDetailedStatisticsService;
        this.stakeholderCategoryService = stakeholderCategoryService;
        this.educationalProgramService = educationalProgramService;
        this.generateReportDtoPreparer = generateReportDtoPreparer;
    }

    @Override
    public void exportReport(GenerateReportForm generateReportForm, HttpServletResponse response) {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=statistics.xls");

        GenerateReportDto generateReportDto = generateReportDtoPreparer.prepare(generateReportForm);

        try (OutputStream outputStream = response.getOutputStream(); Workbook workbook = new HSSFWorkbook()) {

            EducationalProgram educationalProgram = educationalProgramService
                    .findById(generateReportDto.getEducationalProgramId());
            Sheet sheet = workbook.createSheet(educationalProgram.getTitle());

            List<StakeholderCategory> stakeholderCategories = stakeholderCategoryService.findAll();
            Row stakeholderCategoriesRow = sheet.createRow(0);

            CellStyle cellBoldFontStyle = getCellBoldFontStyle(workbook);
            CellStyle cellCenterPositionStyle = getCellCenterPositionStyle(workbook);

            ReportDetailedStatistics reportDetailedStatistics = reportDetailedStatisticsService
                    .calculateReportDetailedStatistics(generateReportDto);

            Map<Long, Long> submissionsCountByStakeholderCategory = reportDetailedStatistics
                    .getSubmissionsCountByStakeholderCategory();

            // Write row with stakeholder category names
            IntStream.range(0, stakeholderCategories.size())
                    .forEach(idx -> {
                        Cell cell = stakeholderCategoriesRow.createCell(idx + 1);

                        StakeholderCategory stakeholderCategory = stakeholderCategories.get(idx);
                        Long submissionsCount = submissionsCountByStakeholderCategory
                                .getOrDefault(stakeholderCategory.getId(), 0L);

                        cell.setCellValue(String.format("%s (%s)", stakeholderCategory.getTitle(), submissionsCount));
                        cell.setCellStyle(cellBoldFontStyle);
                    });

            List<QuestionDetailedStatistics> questionDetailedStatistics = reportDetailedStatistics
                    .getQuestionDetailedStatistics();

            // Write statistics for each question
            IntStream.range(0, questionDetailedStatistics.size()).forEach(rowIdx -> {
                Row questionStatisticsRow = sheet.createRow(rowIdx + 1);

                Cell questionNumberCell = questionStatisticsRow.createCell(0);
                questionNumberCell.setCellStyle(cellBoldFontStyle);

                QuestionDetailedStatistics currentQuestionDetailedStatistic =
                        questionDetailedStatistics.get(rowIdx);

                // Write question number
                questionNumberCell.setCellValue(currentQuestionDetailedStatistic.getQuestionNumber());

                // Write average scores for the question
                IntStream.range(0, stakeholderCategories.size())
                        .forEach(stakeholderCategoryIdx -> {
                            Cell averageScoreCell = questionStatisticsRow
                                    .createCell(stakeholderCategoryIdx + 1);

                            Long stakeholderCategoryId = stakeholderCategories.get(stakeholderCategoryIdx).getId();

                            boolean haveResultForStakeholderCategory = currentQuestionDetailedStatistic
                                    .getAverageScores()
                                    .containsKey(stakeholderCategoryId);

                            if (haveResultForStakeholderCategory) {
                                Double averageScore = currentQuestionDetailedStatistic.getAverageScores()
                                        .get(stakeholderCategoryId);

                                averageScoreCell.setCellValue(averageScore);
                            } else {
                                averageScoreCell.setCellValue("---");
                            }
                            averageScoreCell.setCellStyle(cellCenterPositionStyle);

                        });

            });

            IntStream.range(0, stakeholderCategories.size() + 1).forEach(sheet::autoSizeColumn);

            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("Error while generating Excel report", e);
            throw new ServiceException("Помилка при генерації Excel звіту");
        }

    }

    private CellStyle getCellBoldFontStyle(Workbook workbook) {
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        CellStyle cellStyleForBold = workbook.createCellStyle();
        cellStyleForBold.setFont(boldFont);
        return cellStyleForBold;
    }

    private CellStyle getCellCenterPositionStyle(Workbook workbook) {
        CellStyle cellStyleForBold = workbook.createCellStyle();
        cellStyleForBold.setAlignment(HorizontalAlignment.CENTER);
        return cellStyleForBold;
    }

}
