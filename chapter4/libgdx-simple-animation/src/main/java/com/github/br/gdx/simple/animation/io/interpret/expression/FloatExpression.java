package com.github.br.gdx.simple.animation.io.interpret.expression;

import com.github.br.gdx.simple.animation.fsm.FsmContext;

public class FloatExpression implements Expression {

    private final float number;
    private final String variable;

    public FloatExpression(float number, String variable) {
        this.number = number;
        this.variable = variable;
    }

    @Override
    public boolean gt(FsmContext context) {
        return (float) context.get(variable) > number;
    }

    @Override
    public boolean gtOrEq(FsmContext context) {
        return (float) context.get(variable) >= number;
    }

    @Override
    public boolean lt(FsmContext context) {
        return (float) context.get(variable) < number;
    }

    @Override
    public boolean ltOrEq(FsmContext context) {
        return (float) context.get(variable) <= number;
    }

    @Override
    public boolean eq(FsmContext context) {
        return (float) context.get(variable) == number;
    }

}
