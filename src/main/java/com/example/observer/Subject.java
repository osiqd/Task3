package com.example.observer;

public interface Subject {
    void notifyAllObservers();
    void attach(IObserver obs);
    void detach(IObserver obs);
}
