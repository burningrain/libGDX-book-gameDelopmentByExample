package com.github.br.gdx.simple.animation.fsm;

import com.badlogic.gdx.utils.Array;

import java.util.UUID;

/**
 * Created by user on 09.04.2017.
 */
public class FsmStateSubFsm extends FsmState {

    private final FSM innerFsm;

    public FsmStateSubFsm(String name,
                          boolean startState,
                          boolean endState,
                          StateScript script,
                          FSM innerFsm) {
        super(name, startState, endState, script);
        this.innerFsm = innerFsm;
    }

    @Override
    public void update(FsmContext context) {
        super.update(context);

        FsmContext subContext = context.getSubContext(this.getName());
        Array<FsmTransition> transitions = innerFsm.getState(subContext.getCurrentState()).getTransitions();
        for (FsmTransition t : transitions) {
            if (t.getPredicate().predicate(subContext)) {
                innerFsm.changeState(t.getTo(), subContext);
            }
        }
    }

    public static class Builder {

        private String name = UUID.randomUUID().toString();
        private Array<FsmTransition> transitions = null;

        private boolean startState = false;
        private boolean endState = false;

        private StateScript stateScript = null;


        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder addTransition(FsmTransition transition) {
            if (this.transitions == null) {
                this.transitions = new Array<FsmTransition>();
            }
            this.transitions.add(transition);
            return this;
        }

        public Builder addTransitions(Array<FsmTransition> transitions) {
            if (this.transitions == null) {
                this.transitions = new Array<FsmTransition>();
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
