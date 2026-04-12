package com.wu.web.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class DiaryForm {

    private Long diaryId;

    @NotNull(message = "日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date diaryDate;

    @Size(max = 20, message = "天气描述不能超过20字")
    private String weather;

    @Size(max = 50, message = "心情描述不能超过50字")
    private String mood;

    @Size(max = 100, message = "标题不能超过100字")
    private String title;

    @Size(max = 2000, message = "内容不能超过2000字")
    private String content;

    @Size(max = 500, message = "今日总结不能超过500字")
    private String todaySummary;

    public Long getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(Long diaryId) {
        this.diaryId = diaryId;
    }

    public Date getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(Date diaryDate) {
        this.diaryDate = diaryDate;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTodaySummary() {
        return todaySummary;
    }

    public void setTodaySummary(String todaySummary) {
        this.todaySummary = todaySummary;
    }
}
