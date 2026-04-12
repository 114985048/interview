package com.wu.web;

import com.wu.model.StudyRecord;
import com.wu.model.User;
import com.wu.repository.UserRepository;
import com.wu.service.StudyRecordService;
import com.wu.web.dto.StudyRecordForm;
import com.wu.web.dto.StudySearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * 学习记录控制器
 */
@Controller
@RequestMapping("/study")
public class StudyRecordController {

    @Autowired
    private StudyRecordService studyRecordService;

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取当前登录用户
     */
    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 学习记录列表页
     */
    @GetMapping
    public String list(StudySearchForm searchForm, Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        List<StudyRecord> records = studyRecordService.search(searchForm, user);

        model.addAttribute("records", records);
        model.addAttribute("searchForm", searchForm);

        return "study-list";
    }

    /**
     * 新增学习记录页面
     */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new StudyRecordForm());
        model.addAttribute("isEdit", false);
        return "study-form";
    }

    /**
     * 编辑学习记录页面
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long studyId, Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        StudyRecord record = studyRecordService.findById(studyId, user);

        if (record == null) {
            return "redirect:/study?errorMessage=学习记录不存在";
        }

        StudyRecordForm form = convertToForm(record);
        model.addAttribute("form", form);
        model.addAttribute("isEdit", true);

        return "study-form";
    }

    /**
     * 查看学习记录详情
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long studyId, Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        StudyRecord record = studyRecordService.findById(studyId, user);

        if (record == null) {
            return "redirect:/study?errorMessage=学习记录不存在";
        }

        model.addAttribute("record", record);
        return "study-detail";
    }

    /**
     * 保存学习记录
     */
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("form") StudyRecordForm form,
                       BindingResult result,
                       Authentication authentication,
                       RedirectAttributes redirectAttributes,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", form.getStudyId() != null);
            return "study-form";
        }

        try {
            User user = getCurrentUser(authentication);
            studyRecordService.save(form, user);
            redirectAttributes.addFlashAttribute("successMessage",
                    form.getStudyId() != null ? "学习记录更新成功！" : "学习记录添加成功！");
            return "redirect:/study";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "保存失败：" + e.getMessage());
            model.addAttribute("isEdit", form.getStudyId() != null);
            return "study-form";
        }
    }

    /**
     * 删除学习记录
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long studyId,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(authentication);

        try {
            studyRecordService.delete(studyId, user);
            redirectAttributes.addFlashAttribute("successMessage", "学习记录删除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "删除失败：" + e.getMessage());
        }

        return "redirect:/study";
    }

    /**
     * 将实体转换为表单
     */
    private StudyRecordForm convertToForm(StudyRecord record) {
        StudyRecordForm form = new StudyRecordForm();
        form.setStudyId(record.getStudyId());
        form.setSubject(record.getSubject());
        form.setStudyType(record.getStudyType());
        form.setDifficulty(record.getDifficulty());
        form.setMasteryLevel(record.getMasteryLevel());
        form.setDuration(record.getDuration());
        form.setProgress(record.getProgress());
        form.setContent(record.getContent());
        form.setKeyPoints(record.getKeyPoints());
        form.setQuestions(record.getQuestions());
        form.setStudyDate(record.getStudyDate());
        return form;
    }
}
