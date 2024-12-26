package com.example.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimeServer implements Subject {
    private List<IObserver> observers = new ArrayList<>();
    private int timeState = 0;
    private Timer timer;

    public TimeServer() {
    }

    private void tick() {
        timeState++;
        notifyAllObservers();
    }

    public int getState() {
        return timeState;
    }

    public void setState(int time) {
        this.timeState = time;
    }

    public void attach(IObserver observer) {
        observers.add(observer);
    }

    public void detach(IObserver observer) {
        observers.remove(observer);
    }

    public void notifyAllObservers() {
        for (IObserver observer : observers) {
            observer.update(this);
        }
    }

    public void start() {
        if (timer == null) {
            timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    tick();
                }
            };
            // Запускаем таймер с интервалом 1 секунда
            timer.schedule(task, 0, 1000);
        }
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null; // Обнуляем ссылку на таймер
        }
    }

    // Новый метод для проверки состояния таймера
    public boolean isRunning() {
        return timer != null; // Если таймер не равен null, значит он запущен
    }
}
