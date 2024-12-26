package com.example.observer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class ComponentThree implements IObserver {
    private Timeline colorAnimation;
    private RotateTransition rotateAnimation;
    private Circle animatedCircle;
    private Line clockHand;  // Линия, имитирующая стрелку часов
    private Label animationStatusLabel;

    public ComponentThree(Circle circle, Line clockHand, Label animationStatusLabel) {
        this.animatedCircle = circle;
        this.clockHand = clockHand;
        this.animationStatusLabel = animationStatusLabel;
        createAnimations();
    }

    private void createAnimations() {
        colorAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> animatedCircle.setFill(Color.BLUE)),
                new KeyFrame(Duration.seconds(1), e -> animatedCircle.setFill(Color.RED)),
                new KeyFrame(Duration.seconds(10), e -> animatedCircle.setFill(Color.BLUE))
        );
        colorAnimation.setCycleCount(Timeline.INDEFINITE);

        // Центр вращения устанавливается через положение стрелки
        double centerX = animatedCircle.getLayoutX();
        double centerY = animatedCircle.getLayoutY();
        clockHand.setTranslateX(centerX);
        clockHand.setTranslateY(centerY);

        // Анимация вращения стрелки
        rotateAnimation = new RotateTransition(Duration.seconds(10), clockHand);
        rotateAnimation.setFromAngle(0);
        rotateAnimation.setToAngle(360); // Полный оборот
        rotateAnimation.setCycleCount(RotateTransition.INDEFINITE);
    }

    @Override
    public void update(Subject subject) {
        TimeServer timeServer = (TimeServer) subject;

        if (timeServer.getState() % 10 == 0) {
            String message = "Анимация перезапущена в " + timeServer.getState() + " секунд";

            Platform.runLater(() -> {
                animationStatusLabel.setText(message);
            });
            if (colorAnimation.getStatus() != Timeline.Status.RUNNING) {
                colorAnimation.play();
            }
            if (rotateAnimation.getStatus() != RotateTransition.Status.RUNNING) {
                rotateAnimation.play();
            }
        }
    }

    public void stopAnimation() {
        if (colorAnimation.getStatus() == Timeline.Status.RUNNING) {
            colorAnimation.stop();
        }
        if (rotateAnimation.getStatus() == RotateTransition.Status.RUNNING) {
            rotateAnimation.stop();
            Platform.runLater(() -> {
                clockHand.setRotate(0); // Сбросить положение стрелки
                animationStatusLabel.setText("Анимация остановлена");
            });
        }
    }
}
