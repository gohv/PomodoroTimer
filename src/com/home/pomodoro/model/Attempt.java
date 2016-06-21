package com.home.pomodoro.model;

/**
 * Created by gohv on 09.06.16.
 */
public class Attempt {

    private String message;
    private int remainingSeconds;
    private AttemptKind kind;

    public Attempt(AttemptKind kind, String message) {
        this.message = message;
        this.kind = kind;
        remainingSeconds = kind.getTotalSeconds();
    }

    public String getMessage() {
        return message;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public AttemptKind getKind() {
        return kind;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void tick() {
        remainingSeconds--;
    }

    public void save() {
        //this is a mock method
        System.out.printf("Saving...%s %n", this);
    }

    @Override
    public String toString() {
        return "Attempt{" +
                "message='" + message + '\'' +
                ", remainingSeconds=" + remainingSeconds +
                ", kind=" + kind +
                '}';
    }
}
