package com.wu.model;

/**
 * 掌握程度枚举
 */
public enum MasteryLevel {
    BEGINNER("初学"),
    FAMILIAR("熟悉"),
    PROFICIENT("熟练"),
    MASTER("精通");

    private final String displayName;

    MasteryLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
