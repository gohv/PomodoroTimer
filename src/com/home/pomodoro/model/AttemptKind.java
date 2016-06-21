package com.home.pomodoro.model;


public enum AttemptKind {

    FOCUS(25 * 60, "Focus time"),
    BREAK(5 * 60, "Break time");

    private int totalSeconds;
    private String displayName;

    AttemptKind(int totalSeconds, String displayName) {
        this.totalSeconds = totalSeconds;
        this.displayName = displayName;
    }



    public int getTotalSeconds() {
        return totalSeconds;
    }

    public String getDisplayName() {
        return displayName;
    }
}
