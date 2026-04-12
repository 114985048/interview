package com.wu.model;

/**
 * 难度等级枚举
 */
public enum Difficulty {
    EASY("简单"),
    MEDIUM("中等"),
    HARD("困难");

    private final String displayName;

    Difficulty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
