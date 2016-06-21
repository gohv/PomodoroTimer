package com.home.pomodoro.controllers;

import com.home.pomodoro.model.Attempt;
import com.home.pomodoro.model.AttemptKind;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

public class Home {
    private final AudioClip applause;
    @FXML
    private VBox container;
    @FXML
    private Label title;
    @FXML
    private TextArea message;


    private Attempt currentAttempt;
    //java FX beans:
    private StringProperty timerText;
    private Timeline timeLine;

    public Home() {
        //simple string property implementation:
        timerText = new SimpleStringProperty();
        setTimerText(0);
        applause = new AudioClip(getClass().getResource("/sounds/applause.mp3").toExternalForm());
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
       // resetTimeline();
        clearAttemptStyles();
        currentAttempt = new Attempt(kind, " ");
        addAttemptStyle(kind);
        title.setText(kind.getDisplayName());
        setTimerText(currentAttempt.getRemainingSeconds());
        timeLine = new Timeline();
        timeLine.setCycleCount(kind.getTotalSeconds());
        // get all frames that are curently existing - it is a list
        // add to them and create a brand new keyframe
        timeLine.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
            currentAttempt.tick();
            setTimerText(currentAttempt.getRemainingSeconds());
        }));
        timeLine.setOnFinished(event -> {
            saveCurrentAttempt();
            //throws exception on UBUNTU 16
            applause.play();
            // if the current kind is focus - switch it to break; otherwise switch it to focus
            prepareAttempt(currentAttempt.getKind() == AttemptKind.FOCUS ?
                    AttemptKind.BREAK : AttemptKind.FOCUS);
        });
    }

    private void saveCurrentAttempt() {
        currentAttempt.setMessage(message.getText());
        currentAttempt.save();
    }

    private void resetTimeline() {
        clearAttemptStyles();
        //clearing the bug with the multiple timelines - causing the timer to go faster
        if (timeLine != null && timeLine.getStatus() == Animation.Status.RUNNING) {
            timeLine.stop();
        }
    }

    public void playTimer() {
        container.getStyleClass().add("playing");
        timeLine.play();

    }

    public void pauseTimer() {
        container.getStyleClass().remove("playing");
        timeLine.pause();
    }

    private void addAttemptStyle(AttemptKind kind) {
        //return a list
        container.getStyleClass().add(kind.toString().toLowerCase());
    }

    private void clearAttemptStyles() {
        container.getStyleClass().remove("playing");
        for (AttemptKind kind : AttemptKind.values()) {
            container.getStyleClass().remove(kind.toString().toLowerCase());
        }
    }


    public void restart(ActionEvent actionEvent) {
        prepareAttempt(AttemptKind.FOCUS);
        playTimer();
    }

    public void play(ActionEvent actionEvent) {
        if (currentAttempt == null) {
            restart(actionEvent);
        } else {
            playTimer();
        }
        playTimer();
    }

    public void pause(ActionEvent actionEvent) {
        pauseTimer();

    }
}
