package com.pnu.dev.pnufeedback.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class ApplicationErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Exception e) {

        log.error("Error occurred", e); // ToDo smth weird here

        return "redirect:/";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
