package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.OpenAnswer;
import com.pnu.dev.pnufeedback.dto.ReviewedFilter;
import com.pnu.dev.pnufeedback.service.OpenAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/${app.adminPanelUrl}/open-answers")
public class OpenAnswerController {

    private final OpenAnswerService openAnswerService;

    private final String redirectUrl;

    @Autowired
    public OpenAnswerController(OpenAnswerService openAnswerService,
                                @Value("${app.adminPanelUrl}") String adminPanelUrl) {

        this.openAnswerService = openAnswerService;
        this.redirectUrl = String.format("redirect:/%s/open-answers", adminPanelUrl);
    }

    @GetMapping
    public String findAllUnreviewed(Model model, @PageableDefault(size = 10, sort = "updatedAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OpenAnswer> openAnswersPage = openAnswerService.findAllUnreviewed(pageable);
        model.addAttribute("openAnswersPage", openAnswersPage);

        return "admin/openAnswer/index";
    }

    @GetMapping("/reviewed")
    public String findAllReviewed(@RequestParam(name = "filter", defaultValue = "ALL") ReviewedFilter reviewedFilter,
                                  Model model, @PageableDefault(size = 10, sort = "updatedAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OpenAnswer> openAnswersPage = openAnswerService.findAllReviewed(reviewedFilter, pageable);
        model.addAttribute("openAnswersPage", openAnswersPage);
        model.addAttribute("reviewedFilter", reviewedFilter);

        return "admin/openAnswer/reviewed";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        openAnswerService.approve(id);

        return redirectUrl;
    }

    @PostMapping("/disapprove/{id}")
    public String disapprove(@PathVariable Long id) {
        openAnswerService.disapprove(id);

        return redirectUrl;
    }
}
