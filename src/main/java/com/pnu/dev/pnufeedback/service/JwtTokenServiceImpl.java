package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.GenerateTokenForm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${app.baseUrl}")
    private String appBaseUrl;


    @Override
    public String generateTokenLink(GenerateTokenForm generateTokenForm) {

        Claims claims = Jwts.claims();
        claims.put("educationalProgramId", generateTokenForm.getEducationalProgramId());
        claims.put("stakeholderCategoryId", generateTokenForm.getStakeholderCategoryId());

        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        return appBaseUrl + "?token=" + jwtToken;
    }

}
