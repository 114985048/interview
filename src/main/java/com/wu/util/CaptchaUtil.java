package com.wu.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtil {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final Random RANDOM = new Random();

    public static class Captcha {
        private final String code;
        private final BufferedImage image;

        public Captcha(String code, BufferedImage image) {
            this.code = code;
            this.image = image;
        }

        public String getCode() {
            return code;
        }

        public BufferedImage getImage() {
            return image;
        }
    }

    public static Captcha generateCaptcha() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 28));

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String c = String.valueOf(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
            code.append(c);
            g.setColor(new Color(RANDOM.nextInt(150), RANDOM.nextInt(150), RANDOM.nextInt(150)));
            g.drawString(c, 10 + i * 28, 30);
        }

        for (int i = 0; i < 50; i++) {
            g.setColor(new Color(RANDOM.nextInt(200), RANDOM.nextInt(200), RANDOM.nextInt(200)));
            g.drawLine(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT), RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT));
        }

        g.dispose();
        return new Captcha(code.toString(), image);
    }
}