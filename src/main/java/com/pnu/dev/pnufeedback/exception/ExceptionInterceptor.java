package com.pnu.dev.pnufeedback.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(ServiceException.class)
    public String handleServiceAdminException(ServiceException serviceException, Model model) {
        model.addAttribute("errorMessage", serviceException.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Model model) {
        model.addAttribute("errorMessage", "Внутрішня помилка сервера");
        return "error";
    }

}
