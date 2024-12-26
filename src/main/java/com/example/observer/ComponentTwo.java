package com.example.observer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ComponentTwo implements IObserver {
    private Clip clip;
    private boolean isPlaying = false;
    private int musicStartDelay = 4;
    private int musicStartTime = -1;

    public ComponentTwo() {
        String file = "/music/clock.wav";
        try {
            InputStream audioSrc = getClass().getResourceAsStream(file);
            if (audioSrc == null) {
                throw new IllegalArgumentException("Музыка не найдена: " + file);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = audioSrc.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.addLineListener(event -> {
                if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                    isPlaying = false;
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Subject subject) {
        TimeServer timeServer = (TimeServer) subject;
        int currentTime = timeServer.getState();

        // Если таймер запущен
        if (timeServer.isRunning()) {
            if (musicStartTime == -1) {
                musicStartTime = currentTime + musicStartDelay;
                System.out.println("Время начала музыки установлено на: " + musicStartTime);
            }
            if (!isPlaying && currentTime >= musicStartTime) {
                startMusic();
            }
        } else {
            musicStartTime = -1;
        }
    }

    private void startMusic() {
        if (clip != null) {
            if (!clip.isRunning()) {
                clip.start();
                isPlaying = true;
            }
        }
    }
    public void stopMusic() {
        if (isPlaying) {
            clip.stop();
            isPlaying = false;
        }
    }
}
