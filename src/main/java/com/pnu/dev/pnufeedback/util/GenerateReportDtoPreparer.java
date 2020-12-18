package com.pnu.dev.pnufeedback.util;

import com.pnu.dev.pnufeedback.dto.form.GenerateReportForm;
import com.pnu.dev.pnufeedback.dto.report.GenerateReportDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Component
public class GenerateReportDtoPreparer {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("MM/dd/yyyy")).toFormatter();

    public GenerateReportDto prepare(GenerateReportForm generateReportForm) {

        LocalDate startDate = LocalDate.parse(generateReportForm.getStartDate(), DATE_TIME_FORMATTER);
        LocalDate endDate = LocalDate.parse(generateReportForm.getEndDate(), DATE_TIME_FORMATTER);

        return GenerateReportDto.builder()
                .educationalProgramId(Long.parseLong(generateReportForm.getEducationalProgramId()))
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

}
