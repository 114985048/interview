package com.wu.web.dto;

import com.wu.model.Difficulty;
import com.wu.model.MasteryLevel;
import com.wu.model.StudyType;

/**
 * 学习记录搜索表单
 */
public class StudySearchForm {

    private String subject;
    private StudyType studyType;
    private Difficulty difficulty;
    private MasteryLevel masteryLevel;
    private String sortBy = "studyDate";
    private String direction = "desc";

    // Getters and Setters
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

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
