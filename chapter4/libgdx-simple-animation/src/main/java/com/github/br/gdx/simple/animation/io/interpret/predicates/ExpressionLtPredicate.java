package com.github.br.gdx.simple.animation.io.interpret.predicates;

import com.github.br.gdx.simple.animation.fsm.FsmContext;
import com.github.br.gdx.simple.animation.fsm.FsmPredicate;
import com.github.br.gdx.simple.animation.io.interpret.expression.Expression;

public class ExpressionLtPredicate implements FsmPredicate {

    private final Expression expression;

    public ExpressionLtPredicate(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean predicate(FsmContext context) {
        return expression.lt(context);
    }

}
