package com.github.br.ecs.simple.fsm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by user on 09.04.2017.
 */
public class FSM {

    public static final String ANY_STATE = "anyState";
    FsmContext context;
    FsmState currentState;

    FsmState startState;
    FsmState endState;

    private HashMap<String, FsmState> states = new HashMap<>();
    private ArrayList<FsmChangeStateCallback> fsmStateCallbacks;
    private HashMap<FsmStateEvent, FsmStateEventBasket> rules;

    private final FsmState anyState;
    private LinkedBlockingDeque<FsmStateEvent> fsmEvents = new LinkedBlockingDeque<>();

    //TODO а если TreeSet юзаются? Все с концами?
    FSM(FsmContext context,
        Set<FsmState> states,
        Set<FsmTransition> transitions,
        String startState,
        String endState,
        ArrayList<FsmChangeStateCallback> fsmStateCallbacks,
        Set<FsmStateEventBasket> rules,
        ArrayList<AnyStateTransitionBag> anyStateTransitionBag
        ) {

        this.context = context;
        this.fsmStateCallbacks = fsmStateCallbacks;
        if(rules != null){
            this.rules = new HashMap<>();
            for(FsmStateEventBasket rule : rules){
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
        this.currentState = this.startState;
        this.endState = this.states.get(endState);

        // хак, чтоб как в юнити
        anyState = new FsmState(ANY_STATE, false, false, null);
        anyState.setFsm(this);
        if(anyStateTransitionBag != null){
            for(AnyStateTransitionBag bag : anyStateTransitionBag){
                anyState.addTransition(new FsmTransition(ANY_STATE, bag.to, bag.predicate));
            }
        }
        this.states.put(anyState.getName(), anyState);
    }


    void changeState(String newState) {
        if (this.currentState.getName().equals(newState)) {
            System.out.println(String.format("Вы пытаетесь изменить состояние <%s> на само себя", newState));
            return;
//            throw new IllegalArgumentException(
//                    String.format("Вы пытаетесь изменить состояние <%s> на само себя", newState));
        }
        this.currentState.dispose();
        this.currentState = states.get(newState);
        this.currentState.initialize();
        this.context.setCurrentState(this.currentState.getName());
        if(this.fsmStateCallbacks != null && !this.fsmStateCallbacks.isEmpty()){
            for(FsmChangeStateCallback callback : this.fsmStateCallbacks){
                callback.call(this.context);
            }
        }
    }

    void fireEvent(FsmStateEvent event){
        fsmEvents.addLast(event);
    }

    void handleEvent(FsmStateEvent event){
        this.rules.get(event).notifyHandlers(event);
    }


    public void addChangeStateCallback(FsmChangeStateCallback fsmStateCallback){
        if(fsmStateCallbacks == null){
            fsmStateCallbacks = new ArrayList<>();
        }
        this.fsmStateCallbacks.add(fsmStateCallback);
    }

    public void udpate() {
        while (fsmEvents.peekFirst() != null) {
            handleEvent(fsmEvents.pollFirst());
        }
        this.currentState.update();
        this.anyState.update();
    }

    public String getCurrentState() {
        return currentState.getName();
    }

    public String getStartState() {
        return startState.getName();
    }

    public String getEndState() {
        return endState.getName();
    }

    public FsmContext getContext() {
        return context;
    }

    public static class Builder {
        private FsmContext context;
        private HashSet<FsmState> states = new HashSet<>();
        private HashSet<String> statesString = new HashSet<>();
        private HashSet<FsmStateEventBasket> rules = new HashSet<>();
        private String startState;
        private String endState;
        private ArrayList<FsmChangeStateCallback> fsmStateCallbacks;
        private HashSet<FsmTransition> transitions = new HashSet<>();

        private ArrayList<AnyStateTransitionBag> anyStateTransitionBag;

        public Builder addCallback(FsmChangeStateCallback fsmStateCallback){
            if(fsmStateCallbacks == null){
                fsmStateCallbacks = new ArrayList<>();
            }

            this.fsmStateCallbacks.add(fsmStateCallback);
            return this;
        }

        public Builder addState(FsmState fsmState) {
            if (states.contains(fsmState)) {
                throw new IllegalArgumentException(String.format("Состояние <%s> уже существует!", fsmState.getName()));
            }
            if (fsmState.isStartState()) {
                if(startState != null){
                    throw new IllegalArgumentException("Начальное состояние уже существует!");
                }
                startState = fsmState.getName();
            }
            if (fsmState.isEndState()) {
                if(endState != null){
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

        public Builder addTransitionFromAnyState(String to, FsmPredicate predicate){
            if(anyStateTransitionBag == null){
                anyStateTransitionBag = new ArrayList<>();
            }
            anyStateTransitionBag.add(new AnyStateTransitionBag(to, predicate));
            return this;
        }

        public Builder addGlobalEventRule(FsmStateEventBasket rule){
            this.rules.add(rule);
            return this;
        }

        public Builder addContext(FsmContext context) {
            this.context = context;
            return this;
        }

        public FSM build() {
            //todo проверить на null и выкинуть ошибки
            if(startState == null){
                throw new IllegalStateException("начальное состояние должно быть!");
            }

            return new FSM(context, states, transitions, startState, endState, fsmStateCallbacks, rules, anyStateTransitionBag);
        }


    }

    private static class AnyStateTransitionBag {
        public String to;
        public FsmPredicate predicate;

        public AnyStateTransitionBag(String to, FsmPredicate predicate){
            this.to = to;
            this.predicate = predicate;
        }
    }


}