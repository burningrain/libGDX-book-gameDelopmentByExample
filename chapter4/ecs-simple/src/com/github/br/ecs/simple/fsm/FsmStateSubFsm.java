package com.github.br.ecs.simple.fsm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by user on 09.04.2017.
 */
public class FsmStateSubFsm extends FsmState {

    private FSM innerFsm;

    public FsmStateSubFsm(String name,
                          boolean startState,
                          boolean endState,
                          StateScript script,
                          FSM innerFsm) {
        super(name, startState, endState, script);
        this.innerFsm = innerFsm;
    }

    @Override
    public void update() {
        super.update();
        List<FsmTransition> transitions = innerFsm.currentState.getTransitions();
        for (FsmTransition t : transitions) {
            if (t.getPredicate().predicate(innerFsm.context)) {
                innerFsm.changeState(t.getTo());
            }
        }
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

        public Builder addTranstion(FsmTransition transition) {
            if (this.transitions == null) {
                this.transitions = new ArrayList<FsmTransition>();
            }
            this.transitions.add(transition);
            return this;
        }

        public Builder addTransitions(Collection<FsmTransition> transitions) {
            if (this.transitions == null) {
                this.transitions = new ArrayList<FsmTransition>();
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


        public FsmState build(FSM innerFsm) {
            if (this.name == null) {
                throw new IllegalArgumentException("name не должен быть null");
            }
            FsmStateSubFsm fsmState = new FsmStateSubFsm(this.name, this.startState, this.endState, this.stateScript, innerFsm);
            fsmState.addTransitions(this.transitions);
            return fsmState;
        }
    }

}
