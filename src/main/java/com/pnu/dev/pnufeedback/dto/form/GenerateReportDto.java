package com.pnu.dev.pnufeedback.dto.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class GenerateReportDto {

    @NotNull
    private String educationalProgramId;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;
}
