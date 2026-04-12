package com.wu.web;

import com.wu.model.Diary;
import com.wu.model.User;
import com.wu.repository.UserRepository;
import com.wu.service.DiaryService;
import com.wu.service.UserService;
import com.wu.web.dto.DiaryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;
    private final UserService userService;
    private final UserRepository userRepository;

    private static final List<String> WEATHER_OPTIONS = Arrays.asList(
            "☀️ 晴天", "⛅ 多云", "☁️ 阴天", "🌧️ 雨天", "⛈️ 雷雨", "❄️ 雪天", "🌫️ 雾霾"
    );

    private static final List<String> MOOD_OPTIONS = Arrays.asList(
            "😊 开心", "😄 兴奋", "😌 平静", "😔 低落", "😤 生气", "😰 焦虑", "😴 疲惫", "🤔 思考"
    );

    @Autowired
    public DiaryController(DiaryService diaryService, UserService userService, UserRepository userRepository) {
        this.diaryService = diaryService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listDiaries(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        boolean isAdmin = user.isAdmin();

        Page<Diary> diaryPage;
        long totalCount;

        if (isAdmin) {
            diaryPage = diaryService.findAllWithPage(page, size);
            totalCount = diaryService.countAll();
            // Build userId -> username map for admin view
            Map<Long, String> userNameMap = userRepository.findAll().stream()
                    .collect(Collectors.toMap(User::getUserId, User::getUsername));
            model.addAttribute("userNameMap", userNameMap);
        } else {
            diaryPage = diaryService.findByUserIdWithPage(user.getUserId(), page, size);
            totalCount = diaryService.countByUserId(user.getUserId());
        }

        model.addAttribute("diaries", diaryPage.getContent());
        model.addAttribute("diaryPage", diaryPage);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("isAdmin", isAdmin);

        return "diary-list";
    }

    @GetMapping("/new")
    public String newDiaryForm(Model model) {
        DiaryForm form = new DiaryForm();
        form.setDiaryDate(new Date());
        model.addAttribute("diaryForm", form);
        model.addAttribute("isEdit", false);
        model.addAttribute("weatherOptions", WEATHER_OPTIONS);
        model.addAttribute("moodOptions", MOOD_OPTIONS);
        return "diary-form";
    }

    @GetMapping("/{id}/edit")
    public String editDiaryForm(@PathVariable Long id, Authentication authentication,
                                Model model, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(authentication);
        Diary diary = diaryService.findByIdAndUserId(id, user.getUserId())
                .orElse(null);

        if (diary == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "日记不存在或无权限访问");
            return "redirect:/diary";
        }

        DiaryForm form = convertToForm(diary);
        model.addAttribute("diaryForm", form);
        model.addAttribute("isEdit", true);
        model.addAttribute("weatherOptions", WEATHER_OPTIONS);
        model.addAttribute("moodOptions", MOOD_OPTIONS);
        return "diary-form";
    }

    @PostMapping("/save")
    public String saveDiary(@Valid @ModelAttribute DiaryForm form, BindingResult result,
                           Authentication authentication, RedirectAttributes redirectAttributes,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", form.getDiaryId() != null);
            model.addAttribute("weatherOptions", WEATHER_OPTIONS);
            model.addAttribute("moodOptions", MOOD_OPTIONS);
            return "diary-form";
        }

        User user = getCurrentUser(authentication);

        // 检查同一天是否已有日记（编辑时除外）
        if (form.getDiaryId() == null && diaryService.existsByDate(user.getUserId(), form.getDiaryDate())) {
            model.addAttribute("errorMessage", "该日期已有日记，请编辑现有日记或选择其他日期");
            model.addAttribute("isEdit", false);
            model.addAttribute("weatherOptions", WEATHER_OPTIONS);
            model.addAttribute("moodOptions", MOOD_OPTIONS);
            return "diary-form";
        }

        Diary diary;
        if (form.getDiaryId() != null) {
            diary = diaryService.findByIdAndUserId(form.getDiaryId(), user.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("日记不存在"));
        } else {
            diary = new Diary();
            diary.setUserId(user.getUserId());
        }

        diary.setDiaryDate(form.getDiaryDate());
        diary.setWeather(form.getWeather());
        diary.setMood(form.getMood());
        diary.setTitle(form.getTitle());
        diary.setContent(form.getContent());
        diary.setTodaySummary(form.getTodaySummary());

        diaryService.save(diary);

        redirectAttributes.addFlashAttribute("successMessage",
                form.getDiaryId() != null ? "日记更新成功" : "日记创建成功");
        return "redirect:/diary";
    }

    @GetMapping("/{id}")
    public String viewDiary(@PathVariable Long id, Authentication authentication,
                           Model model, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(authentication);
        Diary diary;

        if (user.isAdmin()) {
            diary = diaryService.findById(id).orElse(null);
        } else {
            diary = diaryService.findByIdAndUserId(id, user.getUserId()).orElse(null);
        }

        if (diary == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "日记不存在或无权限访问");
            return "redirect:/diary";
        }

        model.addAttribute("diary", diary);
        if (user.isAdmin()) {
            userRepository.findById(diary.getUserId()).ifPresent(u ->
                    model.addAttribute("diaryOwner", u.getUsername()));
        }
        return "diary-detail";
    }

    @PostMapping("/{id}/delete")
    public String deleteDiary(@PathVariable Long id, Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(authentication);

        try {
            if (user.isAdmin()) {
                diaryService.deleteById(id);
            } else {
                diaryService.delete(id, user.getUserId());
            }
            redirectAttributes.addFlashAttribute("successMessage", "日记删除成功");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/diary";
    }

    @GetMapping("/search")
    public String searchDiaries(@RequestParam String keyword, Authentication authentication,
                                Model model) {
        User user = getCurrentUser(authentication);
        boolean isAdmin = user.isAdmin();
        List<Diary> diaries;

        if (isAdmin) {
            diaries = diaryService.searchAllByKeyword(keyword);
            Map<Long, String> userNameMap = userRepository.findAll().stream()
                    .collect(Collectors.toMap(User::getUserId, User::getUsername));
            model.addAttribute("userNameMap", userNameMap);
        } else {
            diaries = diaryService.searchByKeyword(user.getUserId(), keyword);
        }

        model.addAttribute("diaries", diaries);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchMode", true);
        model.addAttribute("isAdmin", isAdmin);

        return "diary-list";
    }

    @GetMapping("/today")
    public String todayDiary(Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(authentication);
        Date today = new Date();

        Diary diary = diaryService.findByUserIdAndDate(user.getUserId(), today)
                .orElse(null);

        if (diary != null) {
            return "redirect:/diary/" + diary.getDiaryId() + "/edit";
        } else {
            return "redirect:/diary/new";
        }
    }

    private User getCurrentUser(Authentication authentication) {
        return userService.findByUsername(authentication.getName());
    }

    private DiaryForm convertToForm(Diary diary) {
        DiaryForm form = new DiaryForm();
        form.setDiaryId(diary.getDiaryId());
        form.setDiaryDate(diary.getDiaryDate());
        form.setWeather(diary.getWeather());
        form.setMood(diary.getMood());
        form.setTitle(diary.getTitle());
        form.setContent(diary.getContent());
        form.setTodaySummary(diary.getTodaySummary());
        return form;
    }
}
