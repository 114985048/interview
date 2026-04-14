package com.wu.web;

import com.wu.model.CompanyType;
import com.wu.model.InterviewRecord;
import com.wu.model.InterviewResult;
import com.wu.model.User;
import com.wu.service.InterviewRecordService;
import com.wu.service.UserService;
import com.wu.web.dto.InterviewRecordForm;
import com.wu.web.dto.InterviewSearchForm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/records")
public class InterviewRecordController {

    private final InterviewRecordService interviewRecordService;
    private final UserService userService;

    public InterviewRecordController(InterviewRecordService interviewRecordService, UserService userService) {
        this.interviewRecordService = interviewRecordService;
        this.userService = userService;
    }

    @GetMapping
    public String list(@ModelAttribute("searchForm") InterviewSearchForm searchForm, Model model) {
        User currentUser = getCurrentUser();
        List<InterviewRecord> records = interviewRecordService.search(searchForm, currentUser);
        model.addAttribute("records", records);
        return "record-list";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("当前用户不存在");
        }
        return user;
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        InterviewRecordForm form = new InterviewRecordForm();
        model.addAttribute("recordForm", form);
        model.addAttribute("companyTypes", CompanyType.values());
        model.addAttribute("interviewResults", InterviewResult.values());
        return "record-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("recordForm") InterviewRecordForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companyTypes", CompanyType.values());
            model.addAttribute("interviewResults", InterviewResult.values());
            return "record-form";
        }
        try {
            User currentUser = getCurrentUser();
            interviewRecordService.create(form, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "面试记录新增成功");
            return "redirect:/records";
        } catch (Exception e) {
            model.addAttribute("companyTypes", CompanyType.values());
            model.addAttribute("interviewResults", InterviewResult.values());
            model.addAttribute("errorMessage", e.getMessage());
            return "record-form";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser();
            model.addAttribute("record", interviewRecordService.findById(id, currentUser));
            return "record-detail";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/records";
        }
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser();
            InterviewRecord record = interviewRecordService.findById(id, currentUser);
            model.addAttribute("recordForm", interviewRecordService.toForm(record));
            model.addAttribute("companyTypes", CompanyType.values());
            model.addAttribute("interviewResults", InterviewResult.values());
            return "record-form";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/records";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("recordForm") InterviewRecordForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("companyTypes", CompanyType.values());
            model.addAttribute("interviewResults", InterviewResult.values());
            return "record-form";
        }
        try {
            User currentUser = getCurrentUser();
            interviewRecordService.update(id, form, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "面试记录编辑成功");
            return "redirect:/records/" + id;
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/records";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser();
            interviewRecordService.delete(id, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "删除成功");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/records";
    }

    @GetMapping("/")
    public String indexRedirect() {
        return "redirect:/records";
    }
}
