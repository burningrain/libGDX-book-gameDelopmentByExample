package com.github.br.ecs.simple.fsm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by user on 09.04.2017.
 */
public class FsmState {

    private String name;
    private FSM fsm;
    private ArrayList<FsmTransition> transitions;

    private boolean startState;
    private boolean endState;

    private StateScript stateScript;



    FsmState(String name, boolean startState, boolean endState, StateScript stateScript) {
        this.name = name;
        this.startState = startState;
        this.endState = endState;

        if (stateScript != null) {
            this.stateScript = stateScript;
            stateScript.setFsm(fsm);
        }
    }

    void setFsm(FSM fsm) {
        this.fsm = fsm;
    }

    void addTransition(FsmTransition transition) {
        if (transitions == null) {
            transitions = new ArrayList<>();
        }
        transitions.add(transition);
    }

    void addTransitions(Collection<FsmTransition> transition) {
        if (transitions == null) {
            transitions = new ArrayList<>();
        }
        transitions.addAll(transition);
    }

    public void initialize() {
        if (stateScript != null) {
            stateScript.initialize();
        }
    }

    public void dispose() {
        if (stateScript != null) {
            stateScript.dispose();
        }
    }

    public void update() {
        if (stateScript != null) {
            stateScript.update();
        }
        if (transitions != null) {
            for (FsmTransition t : transitions) {
                if (t.getPredicate().predicate(fsm.context)) {
                    fsm.changeState(t.getTo());
                }
            }
        }
    }

    public StateScript getStateScript() {
        return stateScript;
    }

    public String getName() {
        return name;
    }

    public boolean isStartState() {
        return startState;
    }

    public boolean isEndState() {
        return endState;
    }

    public List<FsmTransition> getTransitions() {
        return transitions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FsmState state = (FsmState) o;

        return !(name != null ? !name.equals(state.name) : state.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public static class Builder {

        private String name = UUID.randomUUID().toString();
        private ArrayList<FsmTransition> transitions = null;

        private boolean startState = false;
        private boolean endState = false;

        private StateScript stateScript = null;


        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        Builder addTranstion(FsmTransition transition) {
            if(this.transitions == null){
                this.transitions = new ArrayList<>();
            }
            this.transitions.add(transition);
            return this;
        }

        Builder addTransitions(Collection<FsmTransition> transitions) {
            if(this.transitions == null){
                this.transitions = new ArrayList<>();
            }
            this.transitions.addAll(transitions);
            return this;
        }

        public Builder setStartState(boolean startState) {
            this.startState = startState;
            return this;
        }

        public Builder setEndState(boolean endState) {
            this.endState = endState;
            return this;
        }

        public Builder setStateScript(StateScript stateScript) {
            this.stateScript = stateScript;
            return this;
        }


        public FsmState build(){
            if(this.name == null){
                throw new IllegalArgumentException("name не должен быть null");
            }
            FsmState fsmState = new FsmState(this.name, this.startState, this.endState, this.stateScript);
            fsmState.transitions = this.transitions;
            return fsmState;
        }
    }
}
