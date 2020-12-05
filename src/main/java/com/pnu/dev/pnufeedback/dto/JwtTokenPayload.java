package com.pnu.dev.pnufeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenPayload {

    private Long educationalProgramId;

    private Long stakeholderCategoryId;

}
