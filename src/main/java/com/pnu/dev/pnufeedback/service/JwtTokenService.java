package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.JwtTokenPayload;
import com.pnu.dev.pnufeedback.dto.form.GenerateTokenForm;

public interface JwtTokenService {

    String generateTokenLink(GenerateTokenForm generateTokenForm);

    JwtTokenPayload resolveTokenPayload(String jwtToken);

}
