package com.pnu.dev.pnufeedback.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StakeholderCategoryForm {

    @NonNull
    private String title;
}