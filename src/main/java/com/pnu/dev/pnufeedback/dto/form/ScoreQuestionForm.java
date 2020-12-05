package com.pnu.dev.pnufeedback.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreQuestionForm {

    @NotNull(message = "Номер запитання повинен бути вказаним")
    private String questionNumber;

    @NotNull(message = "Категорія стейкхолдерів повинна бути вказана")
    private Long stakeholderCategoryId;

    @NotNull(message = "Запитання повинне бути вказаним")
    private String content;
}
