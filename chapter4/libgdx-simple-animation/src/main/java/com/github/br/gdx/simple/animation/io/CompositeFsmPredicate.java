package com.github.br.gdx.simple.animation.io;

import com.github.br.gdx.simple.animation.fsm.FsmContext;
import com.github.br.gdx.simple.animation.fsm.FsmPredicate;

public class CompositeFsmPredicate implements FsmPredicate {

    private final FsmPredicate[] predicates;

    public CompositeFsmPredicate(FsmPredicate[] predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean predicate(FsmContext context) {
        boolean result = true;
        for (FsmPredicate predicate : predicates) {
            result = result && predicate.predicate(context);
        }
        return result;
    }

}
