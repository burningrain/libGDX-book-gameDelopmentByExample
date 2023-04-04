package com.github.br.gdx.simple.animation.io.interpret.expression;

import com.github.br.gdx.simple.animation.fsm.FsmContext;

public class BooleanExpression implements Expression {

    private final boolean exp;
    private final String variable;

    public BooleanExpression(boolean exp, String variable) {
        this.exp = exp;
        this.variable = variable;
    }

    @Override
    public boolean eq(FsmContext context) {
        return (boolean) context.get(variable) == exp;
    }

    @Override
    public boolean gt(FsmContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean gtOrEq(FsmContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean lt(FsmContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean ltOrEq(FsmContext context) {
        throw new UnsupportedOperationException();
    }

}
