package com.pnu.dev.pnufeedback.dto.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class EducationalProgramForm {

    @NotBlank(message = "Назва освітної програми повинна бути вказана")
    private String title;

    private boolean allowedToBeSelectedByUser;

}
