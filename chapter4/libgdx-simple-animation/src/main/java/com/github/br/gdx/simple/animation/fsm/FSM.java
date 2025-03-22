package com.github.br.gdx.simple.animation.fsm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by user on 09.04.2017.
 */
public class FSM {

    public static final String ANY_STATE = "ANY_STATE";

    private final FsmState anyState;
    private final FsmState startState;
    private final FsmState endState;

    private final ObjectMap<String, FsmState> states = new ObjectMap<String, FsmState>();
    private Array<FsmChangeStateCallback> fsmStateCallbacks;
    private ObjectMap<FsmStateEvent, FsmStateEventBasket> rules;

    private final LinkedBlockingDeque<FsmStateEvent> fsmEvents = new LinkedBlockingDeque<FsmStateEvent>(); // TODO нужна ли конкуррентная коллекция?

    //TODO а если TreeSet юзаются? Все с концами?
    FSM(
            ObjectSet<FsmState> states,
            ObjectSet<FsmTransition> transitions,
            String startState,
            String endState,
            Array<FsmChangeStateCallback> fsmStateCallbacks,
            ObjectSet<FsmStateEventBasket> rules,
            Array<AnyStateTransitionBag> anyStateTransitionBag
    ) {
        this.fsmStateCallbacks = fsmStateCallbacks;
        if (rules != null) {
            this.rules = new ObjectMap<FsmStateEvent, FsmStateEventBasket>();
            for (FsmStateEventBasket rule : rules) {
                this.rules.put(rule.getEvent(), rule);
            }
        }
        for (FsmState fsmState : states) {
            fsmState.setFsm(this);
            this.states.put(fsmState.getName(), fsmState);
        }
        for (FsmTransition t : transitions) {
            FsmState s = this.states.get(t.getFrom());
            s.addTransition(t);
        }
        this.startState = this.states.get(startState);
        if(endState != null) {
            this.endState = this.states.get(endState);
        } else {
            this.endState = null;
        }

        // хак, чтоб как в юнити
        anyState = new FsmState(ANY_STATE, false, false, null);
        anyState.setFsm(this);
        if (anyStateTransitionBag != null) {
            for (AnyStateTransitionBag bag : anyStateTransitionBag) {
                anyState.addTransition(new FsmTransition(ANY_STATE, bag.to, bag.predicate));
            }
        }
        this.states.put(anyState.getName(), anyState);
    }

    FsmState getState(String stateName) {
        return this.states.get(stateName);
    }

    void changeState(String newState, FsmContext context) {
        FsmState currentState = states.get(context.getCurrentState());
        if (currentState.getName().equals(newState)) {
            Gdx.app.debug("FSM", "Вы пытаетесь изменить состояние <" + newState + "> на само себя");
            return;
        }
        currentState.dispose();
        currentState = states.get(newState);

        currentState.initialize();
        context.setCurrentState(currentState.getName());
        if (this.fsmStateCallbacks != null && this.fsmStateCallbacks.size != 0) {
            for (FsmChangeStateCallback callback : this.fsmStateCallbacks) {
                callback.call(context);
            }
        }
    }

    void fireEvent(FsmStateEvent event) {
        fsmEvents.addLast(event);
    }

    void handleEvent(FsmStateEvent event) {
        this.rules.get(event).notifyHandlers(event);
    }


    public void addChangeStateCallback(FsmChangeStateCallback fsmStateCallback) {
        if (fsmStateCallbacks == null) {
            fsmStateCallbacks = new Array<FsmChangeStateCallback>();
        }
        this.fsmStateCallbacks.add(fsmStateCallback);
    }

    public void update(FsmContext context) {
        while (fsmEvents.peekFirst() != null) {
            handleEvent(fsmEvents.pollFirst());
        }
        FsmState currentState = states.get(context.getCurrentState());
        currentState.update(context);
        this.anyState.update(context);
    }

    public String getStartState() {
        return startState.getName();
    }

    public String getEndState() {
        return endState.getName();
    }

    public static class Builder {
        private ObjectSet<FsmState> states = new ObjectSet<FsmState>();
        private ObjectSet<String> statesString = new ObjectSet<String>();
        private ObjectSet<FsmStateEventBasket> rules = new ObjectSet<FsmStateEventBasket>();
        private String startState;
        private String endState;
        private Array<FsmChangeStateCallback> fsmStateCallbacks;
        private ObjectSet<FsmTransition> transitions = new ObjectSet<FsmTransition>();

        private Array<AnyStateTransitionBag> anyStateTransitionBag;

        public Builder addCallback(FsmChangeStateCallback fsmStateCallback) {
            if (fsmStateCallbacks == null) {
                fsmStateCallbacks = new Array<FsmChangeStateCallback>();
            }

            this.fsmStateCallbacks.add(fsmStateCallback);
            return this;
        }

        public Builder addState(FsmState fsmState) {
            if (states.contains(fsmState)) {
                throw new IllegalArgumentException(String.format("Состояние <%s> уже существует!", fsmState.getName()));
            }
            if (fsmState.isStartState()) {
                if (startState != null) {
                    throw new IllegalArgumentException("Начальное состояние уже существует!");
                }
                startState = fsmState.getName();
            }
            if (fsmState.isEndState()) {
                if (endState != null) {
                    throw new IllegalArgumentException("Конечное состояние уже существует!");
                }
                endState = fsmState.getName();
            }
            states.add(fsmState);
            statesString.add(fsmState.getName());
            return this;
        }

        public Builder addTransition(String from, String to, FsmPredicate predicate) {
            if (!statesString.contains(from) || !statesString.contains(to)) {
                throw new IllegalArgumentException(String.format("Состояние <%s> или <%s> не существует!", from, to));
            }
            FsmTransition transition = new FsmTransition(from, to, predicate);
            transitions.add(transition);

            return this;
        }

        public Builder addTransitionFromAnyState(String to, FsmPredicate predicate) {
            if (anyStateTransitionBag == null) {
                anyStateTransitionBag = new Array<AnyStateTransitionBag>();
            }
            anyStateTransitionBag.add(new AnyStateTransitionBag(to, predicate));
            return this;
        }

        public Builder addGlobalEventRule(FsmStateEventBasket rule) {
            this.rules.add(rule);
            return this;
        }

        public FSM build() {
            if (startState == null) {
                throw new IllegalStateException("начальное состояние должно быть задано!");
            }

            return new FSM(states, transitions, startState, endState, fsmStateCallbacks, rules, anyStateTransitionBag);
        }


    }

    private static class AnyStateTransitionBag {
        public final String to;
        public final FsmPredicate predicate;

        public AnyStateTransitionBag(String to, FsmPredicate predicate) {
            this.to = to;
            this.predicate = predicate;
        }
    }


}