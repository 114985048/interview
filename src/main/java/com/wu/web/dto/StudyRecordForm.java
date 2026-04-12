package com.wu.web.dto;

import com.wu.model.Difficulty;
import com.wu.model.MasteryLevel;
import com.wu.model.StudyType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 学习记录表单
 */
public class StudyRecordForm {

    private Long studyId;

    @NotBlank(message = "学习主题不能为空")
    @Size(max = 200, message = "学习主题不能超过200个字符")
    private String subject;

    @NotNull(message = "请选择学习类型")
    private StudyType studyType;

    @NotNull(message = "请选择难度等级")
    private Difficulty difficulty;

    @NotNull(message = "请选择掌握程度")
    private MasteryLevel masteryLevel;

    @NotNull(message = "学习时长不能为空")
    @Min(value = 1, message = "学习时长至少为1分钟")
    @Max(value = 1440, message = "学习时长不能超过24小时")
    private Integer duration;

    @NotNull(message = "学习进度不能为空")
    @Min(value = 0, message = "学习进度不能小于0")
    @Max(value = 100, message = "学习进度不能超过100")
    private Integer progress;

    @Size(max = 5000, message = "学习内容不能超过5000个字符")
    private String content;

    @Size(max = 2000, message = "重点笔记不能超过2000个字符")
    private String keyPoints;

    @Size(max = 2000, message = "疑问记录不能超过2000个字符")
    private String questions;

    @NotNull(message = "学习时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime studyDate;

    public StudyRecordForm() {
        this.progress = 0;
        this.studyDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getStudyId() {
        return studyId;
    }

    public void setStudyId(Long studyId) {
        this.studyId = studyId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public StudyType getStudyType() {
        return studyType;
    }

    public void setStudyType(StudyType studyType) {
        this.studyType = studyType;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public MasteryLevel getMasteryLevel() {
        return masteryLevel;
    }

    public void setMasteryLevel(MasteryLevel masteryLevel) {
        this.masteryLevel = masteryLevel;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(String keyPoints) {
        this.keyPoints = keyPoints;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public LocalDateTime getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(LocalDateTime studyDate) {
        this.studyDate = studyDate;
    }
}
