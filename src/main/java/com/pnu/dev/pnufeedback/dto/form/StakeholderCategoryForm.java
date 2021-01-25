package com.pnu.dev.pnufeedback.dto.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class StakeholderCategoryForm {

    @NotBlank(message = "Назва категорії стейкхолдерів повинна бути вказана")
    private String title;

    private boolean showInReport;

}
