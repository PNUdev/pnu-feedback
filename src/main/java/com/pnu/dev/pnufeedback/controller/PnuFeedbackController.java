package com.pnu.dev.pnufeedback.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class PnuFeedbackController {

  @Value("${app.adminEmail}")
  private String adminEmail;

  @GetMapping
  public String index(Model model) {
    model.addAttribute("adminEmail", adminEmail);
    return "main/index";
  }

}
