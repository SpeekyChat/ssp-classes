package org.restudios.gateway.client.ssp;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class StateAndListener<T> {
    private final Set<Consumer<T>> listeners = new HashSet<>();
    @Getter
    private T state;

    public StateAndListener(T state) {
        this.state = state;
    }

    public Runnable addListener(Consumer<T> listener) {
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    public void setState(T state) {
        this.state = state;
        this.notifyListeners();
    }

    public void notifyListeners() {
        listeners.forEach(listener -> {
            try {
                listener.accept(state);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        });
    }
}
