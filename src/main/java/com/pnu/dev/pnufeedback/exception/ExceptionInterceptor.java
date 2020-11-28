package com.pnu.dev.pnufeedback.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(ServiceException.class)
    public String handleServiceAdminException(ServiceException serviceException) {
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException() {
        return "error";
    }

}
