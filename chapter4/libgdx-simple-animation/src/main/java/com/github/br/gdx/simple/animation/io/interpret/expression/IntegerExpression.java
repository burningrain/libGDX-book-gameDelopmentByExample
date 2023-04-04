package com.github.br.gdx.simple.animation.io.interpret.expression;

import com.github.br.gdx.simple.animation.fsm.FsmContext;

public class IntegerExpression implements Expression {

    private final int number;
    private final String variable;

    public IntegerExpression(int number, String variable) {
        this.number = number;
        this.variable = variable;
    }

    @Override
    public boolean gt(FsmContext context) {
        return (int) context.get(variable) > number;
    }

    @Override
    public boolean gtOrEq(FsmContext context) {
        return (int) context.get(variable) >= number;
    }

    @Override
    public boolean lt(FsmContext context) {
        return (int) context.get(variable) < number;
    }

    @Override
    public boolean ltOrEq(FsmContext context) {
        return (int) context.get(variable) <= number;
    }

    @Override
    public boolean eq(FsmContext context) {
        return (int) context.get(variable) == number;
    }

}
