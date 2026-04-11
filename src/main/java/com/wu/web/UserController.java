package com.wu.web;

import com.wu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("captcha") String captcha,
                           RedirectAttributes redirectAttributes,
                           HttpSession session) {
        try {
            String sessionCaptcha = (String) session.getAttribute(CaptchaController.CAPTCHA_SESSION_KEY);
            if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
                redirectAttributes.addFlashAttribute("errorMessage", "验证码错误");
                return "redirect:/register";
            }
            session.removeAttribute(CaptchaController.CAPTCHA_SESSION_KEY);
            
            userService.register(username, password);
            redirectAttributes.addFlashAttribute("successMessage", "注册成功，请登录");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }
}