package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.GenerateTokenForm;

public interface JwtTokenService {

    String generateTokenLink(GenerateTokenForm generateTokenForm);

}
