package com.wu.model;

/**
 * 学习类型枚举
 */
public enum StudyType {
    TECH("技术学习"),
    ALGORITHM("算法练习"),
    PROJECT("项目实战"),
    READING("阅读笔记"),
    OTHER("其他");

    private final String displayName;

    StudyType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
