package com.packt.flappeebee;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 28.02.2017.
 */
public class GamePublisher {

    private static GamePublisher machine = new GamePublisher();

    public static GamePublisher self() {
        return machine;
    }

    private GamePublisher() {
    }

    private State currentState = State.NOT_STARTED;

    public void changeState(State newState) {
        if (newState == null) throw new IllegalArgumentException();

        this.currentState = newState;
        notifyListeners(newState);
    }

    public State getCurrentState() {
        return currentState;
    }

    private HashMap<State, ArrayList<Subscriber>> subscribers = new HashMap<State, ArrayList<Subscriber>>();

    public void addListener(State state, Subscriber subscriber) {
        if (state == null) throw new IllegalArgumentException();
        if (subscriber == null) throw new IllegalArgumentException();

        if (subscribers.get(state) == null) {
            ArrayList<Subscriber> list = new ArrayList<Subscriber>();
            list.add(subscriber);
            subscribers.put(state, list);
        } else {
            subscribers.get(state).add(subscriber);
        }
    }

    public void removeListener(State state, Subscriber subscriber) {
        if (subscribers.get(state) != null) {
            subscribers.get(state).remove(subscriber);
        }
    }

    private void notifyListeners(State state) {
        ArrayList<Subscriber> list = subscribers.get(state);
        if (list != null && !list.isEmpty()) {
            for (Subscriber subscriber : list) {
                subscriber.handleGameState(state);
            }
        }
    }

    public enum State {
        NOT_STARTED, NEW_GAME, PLAYING, GAME_OVER
    }

    public interface Subscriber {
        void handleGameState(State state);
    }

}
