package com.pnu.dev.pnufeedback.controller;

import com.pnu.dev.pnufeedback.domain.StakeholderCategory;
import com.pnu.dev.pnufeedback.dto.form.StakeholderCategoryForm;
import com.pnu.dev.pnufeedback.service.StakeholderCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/${app.adminPanelUrl}/stakeholder-categories")
public class StakeholderCategoryController {

    private final StakeholderCategoryService stakeholderCategoryService;

    @Value("${app.adminPanelUrl}")
    private final String redirectUrl;

    @Autowired
    public StakeholderCategoryController(StakeholderCategoryService stakeholderCategoryService,
                                         @Value("${app.adminPanelUrl}") String adminPanelUrl) {

        this.stakeholderCategoryService = stakeholderCategoryService;
        this.redirectUrl = String.format("redirect:/%s/stakeholder-categories", adminPanelUrl);
    }

    @GetMapping
    public String findAll(Model model) {
        List<StakeholderCategory> stakeholderCategories = stakeholderCategoryService.findAll();
        model.addAttribute("stakeholderCategories", stakeholderCategories);
        return "admin/stakeholderCategory/index";
    }


    @GetMapping("/new")
    public String createForm() {
        return "admin/stakeholderCategory/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        StakeholderCategory stakeholderCategory = stakeholderCategoryService.findById(id);
        model.addAttribute("stakeholderCategory", stakeholderCategory);

        return "admin/stakeholderCategory/form";
    }

    @GetMapping("/delete/{id}") // the {id} should be in the url
    public String deleteConfirmation(Model model,
                                     @RequestHeader(value = "referer", defaultValue = "/") String returnBackUrl) {

        model.addAttribute("message", "Ви точно хочете видалити категорію стейкхолдерів?");
        model.addAttribute("returnBackUrl", returnBackUrl);

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/new")
    public String create(@Validated StakeholderCategoryForm stakeholderCategoryForm) {

        stakeholderCategoryService.create(stakeholderCategoryForm);

        return redirectUrl;
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Validated StakeholderCategoryForm stakeholderCategoryForm) {

        stakeholderCategoryService.update(id, stakeholderCategoryForm);

        return redirectUrl;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {

        stakeholderCategoryService.delete(id);

        return redirectUrl;
    }
}
