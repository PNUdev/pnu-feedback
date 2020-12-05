package com.pnu.dev.pnufeedback.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationalProgramForm {

    @NotNull(message = "Назва освітної програми повинна бути вказана")
    private String title;

}
