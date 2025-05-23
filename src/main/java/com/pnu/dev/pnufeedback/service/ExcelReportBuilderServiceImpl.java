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
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.stream.Collectors;
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

        GenerateReportDto generateReportDto = generateReportDtoPreparer.prepare(generateReportForm);

        ReportDetailedStatistics reportDetailedStatistics = reportDetailedStatisticsService
                .calculateReportDetailedStatistics(generateReportDto);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s--%s.xls",
                generateReportDto.getStartDate().toLocalDate(), generateReportDto.getEndDate().toLocalDate()));

        try (OutputStream outputStream = response.getOutputStream(); Workbook workbook = new HSSFWorkbook()) {

            EducationalProgram educationalProgram = educationalProgramService
                    .findById(generateReportDto.getEducationalProgramId());
            Sheet sheet = workbook.createSheet(educationalProgram.getTitle());

            List<StakeholderCategory> stakeholderCategories = stakeholderCategoryService
                .findAllToShowInReport().stream()
                .filter(stakeholder ->
                    reportDetailedStatistics.getSubmissionsCountByStakeholderCategory().containsKey(stakeholder.getId())
                )
                .collect(Collectors.toList());

            Row stakeholderCategoriesRow = sheet.createRow(0);

            CellStyle cellBoldFontStyle = getCellBoldFontStyle(workbook);
            CellStyle cellCenterPositionStyle = getCellCenterPositionStyle(workbook);
            CellStyle cellGreenColorStyle = getCellColorStyle(workbook, IndexedColors.LIGHT_GREEN);
            CellStyle cellYellowColorStyle = getCellColorStyle(workbook, IndexedColors.LIGHT_YELLOW);
            CellStyle cellRedColorStyle = getCellColorStyle(workbook, IndexedColors.RED);


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

            // Empty cell
            stakeholderCategoriesRow.createCell(stakeholderCategoriesRow.getLastCellNum());

            // Write a pair of stakeholder category names
            IntStream.range(0, stakeholderCategories.size())
                     .forEach(i -> {
                         String categoryTitle1 = stakeholderCategories.get(i).getTitle();

                         IntStream.range(i+1, stakeholderCategories.size())
                                  .forEach(j -> {
                                      String categoryTitle2 = stakeholderCategories.get(j).getTitle();

                                      Cell cell = stakeholderCategoriesRow.createCell(
                                          stakeholderCategoriesRow.getLastCellNum()
                                      );

                                      cell.setCellValue(String.format("%s%n  /%n%s", categoryTitle1, categoryTitle2));
                                      cell.setCellStyle(cellBoldFontStyle);
                                  });
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
                        .forEach(cellIdx -> {
                            Cell averageScoreCell = questionStatisticsRow
                                    .createCell(cellIdx + 1);

                            Long stakeholderCategoryId = stakeholderCategories.get(cellIdx).getId();

                            boolean haveResultForStakeholderCategory = currentQuestionDetailedStatistic
                                    .getAverageScores()
                                    .containsKey(stakeholderCategoryId);

                            if (haveResultForStakeholderCategory) {
                                Double averageScore = currentQuestionDetailedStatistic.getAverageScores()
                                        .get(stakeholderCategoryId);

                                averageScoreCell.setCellFormula(String.format(Locale.US,"%.2f", averageScore));
                            } else {
                                averageScoreCell.setCellValue("---");
                            }
                            averageScoreCell.setCellStyle(cellCenterPositionStyle);

                        });

                // Empty cell
                questionStatisticsRow.createCell(questionStatisticsRow.getLastCellNum());

                // Write discrepancy levels for the question
                IntStream.range(0, stakeholderCategories.size())
                         .forEach(i -> {
                             Long stakeholderCategoryId1 = stakeholderCategories.get(i).getId();

                             IntStream.range(i+1, stakeholderCategories.size())
                                      .forEach(j -> {
                                          Long stakeholderCategoryId2 = stakeholderCategories.get(j).getId();

                                          Cell discrepancyScoreCell = questionStatisticsRow.createCell(
                                              questionStatisticsRow.getLastCellNum()
                                          );

                                          Double discrepancy = currentQuestionDetailedStatistic
                                              .getDiscrepancyBetween(stakeholderCategoryId1, stakeholderCategoryId2);

                                          if (discrepancy == null) {
                                              return;
                                          }

                                          discrepancyScoreCell.setCellFormula(
                                              String.format(Locale.US, "%.2f", discrepancy)
                                          );

                                          if (discrepancy >= 3.5) {
                                              discrepancyScoreCell.setCellStyle(cellRedColorStyle);
                                          } else if (discrepancy >= 1.5) {
                                              discrepancyScoreCell.setCellStyle(cellYellowColorStyle);
                                          } else if (discrepancy >= 1) {
                                              discrepancyScoreCell.setCellStyle(cellGreenColorStyle);
                                          } else {
                                              discrepancyScoreCell.setCellStyle(cellCenterPositionStyle);
                                          }

                                      });
                         });
            });

            IntStream.range(0, stakeholderCategories.size() + 2 + getUniquePairsNumber(stakeholderCategories.size()))
                     .forEach(sheet::autoSizeColumn);

            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("Error while generating Excel report", e);
            throw new ServiceException("Помилка при генерації Excel звіту");
        }

    }

    private int getUniquePairsNumber(int n) {
        return (n * (n - 1)) / 2;
    }

    private CellStyle getCellBoldFontStyle(Workbook workbook) {
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        CellStyle cellStyleForBold = workbook.createCellStyle();
        cellStyleForBold.setFont(boldFont);
        cellStyleForBold.setAlignment(HorizontalAlignment.CENTER);
        cellStyleForBold.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleForBold.setWrapText(true);
        return cellStyleForBold;
    }

    private CellStyle getCellCenterPositionStyle(Workbook workbook) {
        CellStyle cellStyleForBold = workbook.createCellStyle();
        cellStyleForBold.setAlignment(HorizontalAlignment.CENTER);
        return cellStyleForBold;
    }

    private CellStyle getCellColorStyle(Workbook workbook, IndexedColors color) {
        CellStyle cellStyleForColor = workbook.createCellStyle();
        cellStyleForColor.setFillForegroundColor(color.getIndex());
        cellStyleForColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyleForColor.setAlignment(HorizontalAlignment.CENTER);
        return cellStyleForColor;
    }

}
