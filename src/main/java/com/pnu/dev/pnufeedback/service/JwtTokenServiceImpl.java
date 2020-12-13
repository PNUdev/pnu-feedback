package com.pnu.dev.pnufeedback.service;

import com.pnu.dev.pnufeedback.dto.JwtTokenPayload;
import com.pnu.dev.pnufeedback.dto.form.GenerateTokenForm;
import com.pnu.dev.pnufeedback.exception.ServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private static final String EDUCATIONAL_PROGRAM_ID = "e";

    private static final String STAKEHOLDER_CATEGORY_ID = "s";

    private static final String ALLOW_TO_CHOOSE_EDUCATIONAL_PROGRAM_ID = "c";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${app.baseUrl}")
    private String appBaseUrl;

    @Override
    public String generateTokenLink(GenerateTokenForm generateTokenForm) {

        Claims claims = Jwts.claims();
        claims.put(EDUCATIONAL_PROGRAM_ID, generateTokenForm.getEducationalProgramId());
        claims.put(STAKEHOLDER_CATEGORY_ID, generateTokenForm.getStakeholderCategoryId());
        claims.put(ALLOW_TO_CHOOSE_EDUCATIONAL_PROGRAM_ID, generateTokenForm.isAllowToChooseEducationalProgram());

        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        return appBaseUrl + "/feedback?token=" + jwtToken;
    }

    @Override
    public JwtTokenPayload resolveTokenPayload(String jwtToken) {

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwtToken)
                    .getBody();

            String educationalProgramId = claims.get(EDUCATIONAL_PROGRAM_ID, String.class);
            String stakeholderCategoryId = claims.get(STAKEHOLDER_CATEGORY_ID, String.class);
            Boolean allowToChooseEducationalProgram = claims
                    .get(ALLOW_TO_CHOOSE_EDUCATIONAL_PROGRAM_ID, Boolean.class);

            return JwtTokenPayload.builder()
                    .educationalProgramId(convertEducationalProgramId(educationalProgramId))
                    .stakeholderCategoryId(Long.parseLong(stakeholderCategoryId))
                    .allowToChooseEducationalProgram(allowToChooseEducationalProgram)
                    .build();

        } catch (JwtException | IllegalArgumentException e) {
            throw new ServiceException("Невалідне посилання");
        }

    }

    private Long convertEducationalProgramId(String educationalProgramId) {
        return nonNull(educationalProgramId) ? Long.parseLong(educationalProgramId) : null;
    }

}
