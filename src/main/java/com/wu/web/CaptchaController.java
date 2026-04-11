package com.wu.web;

import com.wu.util.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class CaptchaController {

    public static final String CAPTCHA_SESSION_KEY = "captcha_code";

    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response, HttpSession session) throws IOException {
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setDateHeader("Expires", 0);

        CaptchaUtil.Captcha captcha = CaptchaUtil.generateCaptcha();
        session.setAttribute(CAPTCHA_SESSION_KEY, captcha.getCode());

        ImageIO.write(captcha.getImage(), "PNG", response.getOutputStream());
    }
}