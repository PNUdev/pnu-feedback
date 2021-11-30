package com.pnu.dev.pnufeedback.aop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Objects;

@ControllerAdvice
public class ModelAttributeSetter {

    @Value("${app.adminPanelUrl}")
    private String adminPanelUrl;

    @ModelAttribute
    public void setAdminPanelUrl(@AuthenticationPrincipal User user, Model model) {
        if (Objects.nonNull(user)) {
            model.addAttribute("adminPanelUrl", adminPanelUrl);
        }
    }
}

