package com.wu.model;

/**
 * 面试结果枚举
 */
public enum InterviewResult {
    PENDING("待结果"),
    PASSED("已通过"),
    FAILED("未通过"),
    GAVE_UP("已放弃");

    private final String displayName;

    InterviewResult(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
