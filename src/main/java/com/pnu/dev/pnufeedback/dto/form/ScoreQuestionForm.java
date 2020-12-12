package com.pnu.dev.pnufeedback.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ScoreQuestionForm {

    @NotBlank(message = "Номер запитання повинен бути вказаним")
    private String questionNumber;

    @NotNull
    private Long stakeholderCategoryId;

    @NotBlank(message = "Запитання повинне бути вказаним")
    private String content;
}
