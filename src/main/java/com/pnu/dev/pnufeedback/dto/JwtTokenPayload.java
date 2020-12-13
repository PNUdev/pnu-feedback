package com.pnu.dev.pnufeedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenPayload {

    private Long educationalProgramId;

    private Long stakeholderCategoryId;

}
