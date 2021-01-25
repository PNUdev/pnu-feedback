package com.pnu.dev.pnufeedback.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler({ServiceException.class})
    public String handleServiceAdminException(ServiceException serviceException, Model model,
                                              HttpServletRequest servletRequest) {

        log.error("Service exception occurred! Message: {}", serviceException.getMessage());

        model.addAttribute("previousLocation", servletRequest.getHeader("referer"));
        model.addAttribute("errorMessage", serviceException.getMessage());

        return "main/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception) {

        log.error("Exception occurred!", exception);

        return "redirect:/";
    }

    @ExceptionHandler(EmptyReportException.class)
    public String handleException(EmptyReportException exception, RedirectAttributes redirectAttributes) {

        log.error("Empty report exception occurred. Message: {}", exception.getMessage());

        redirectAttributes.addFlashAttribute("warningMessage", exception.getMessage());

        return "redirect:/admin/generate-report";
    }

}
