package com.home.pomodoro.controllers;

import com.home.pomodoro.model.Attempt;
import com.home.pomodoro.model.AttemptKind;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Home {
    @FXML
    private VBox container;
    @FXML
    private Label title;


    private Attempt currentAttempt;
    //java FX beans:
    private StringProperty timerText;
    private Timeline timeLine;

    public Home() {
        //simple string property implementation:
        timerText = new SimpleStringProperty();
        setTimerText(0);
    }

    public String getTimerText() {
        return timerText.get();
    }

    public StringProperty timerTextProperty() {
        return timerText;
    }

    public void setTimerText(String timerText) {
        this.timerText.set(timerText);
    }

    public void setTimerText(int remainingSeconds) {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        setTimerText(String.format("%02d:%02d", minutes, seconds));
    }

    private void prepareAttempt(AttemptKind kind) {
        clearAttemptStyles();
        currentAttempt = new Attempt(kind, " ");
        addAttemptStyle(kind);
        title.setText(kind.getDisplayName());
        setTimerText(currentAttempt.getRemainingSeconds());
        //TODO: This is creating multiple timelins and causing the timer to go faster !!!
        timeLine = new Timeline();
        timeLine.setCycleCount(kind.getTotalSeconds());
        // get all frames that are curently existing - it is a list
        // add to them and create a brand new keyframe
        timeLine.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
            currentAttempt.tick();
            setTimerText(currentAttempt.getRemainingSeconds());
        }));
    }

    public void playTimer(){
        timeLine.play();

    }

    public void pauseTimer(){
        timeLine.pause();
    }

    private void addAttemptStyle(AttemptKind kind) {
        //return a list
        container.getStyleClass().add(kind.toString().toLowerCase());
    }

    private void clearAttemptStyles() {
        for (AttemptKind kind : AttemptKind.values()) {
            container.getStyleClass().remove(kind.toString().toLowerCase());
        }
    }

    public void debug(ActionEvent actionEvent) {
        System.out.println("DEBUG");
    }

    public void restart(ActionEvent actionEvent) {
        prepareAttempt(AttemptKind.FOCUS);
        playTimer();
    }
}
