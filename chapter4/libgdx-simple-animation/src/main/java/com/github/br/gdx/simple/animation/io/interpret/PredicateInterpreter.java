package com.github.br.gdx.simple.animation.io.interpret;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.fsm.FsmPredicate;
import com.github.br.gdx.simple.animation.io.interpret.expression.BooleanExpression;
import com.github.br.gdx.simple.animation.io.interpret.expression.Expression;
import com.github.br.gdx.simple.animation.io.interpret.expression.FloatExpression;
import com.github.br.gdx.simple.animation.io.interpret.expression.IntegerExpression;
import com.github.br.gdx.simple.animation.io.interpret.predicates.*;

public class PredicateInterpreter {

    public FsmPredicate interpret(ObjectMap<String, String> variables, String fsmPredicate) {
        String[] tokens = fsmPredicate.split(" ");
        if (tokens.length != 3) {
            throw new IllegalArgumentException(
                    "The expression must contain only 3 tokens in right order:\n " +
                            "1) a variable name\n 2) an operator (<=, <, >, >=, ==)\n 3) (Integer, Boolean, Float)\n Example:\nMOVEMENT >= 25.6f");
        }

        TypeEnum type = TypeEnum.getByValue(variables.get(tokens[0]));
        OperatorEnum operator = OperatorEnum.getByValue(tokens[1]);

        Expression expression = createExpression(type, tokens[0], tokens[2]);
        return createPredicate(operator, expression);
    }

    private FsmPredicate createPredicate(OperatorEnum operator, Expression expression) {
        switch (operator) {
            case EQ:
                return new ExpressionEqPredicate(expression);
            case GT:
                return new ExpressionGtPredicate(expression);
            case LT:
                return new ExpressionLtPredicate(expression);
            case GT_OR_EQ:
                return new ExpressionGtOrEqPredicate(expression);
            case LT_OR_EQ:
                return new ExpressionLtOrEqPredicate(expression);
            default:
                throw new IllegalArgumentException("The operator [" + operator + "] is not supported");
        }
    }

    private Expression createExpression(TypeEnum type, String variable, String value) {
        switch (type) {
            case INTEGER:
                return new IntegerExpression(Integer.parseInt(value), variable);
            case FLOAT:
                return new FloatExpression(Float.parseFloat(value), variable);
            case BOOLEAN:
                return new BooleanExpression(Boolean.parseBoolean(value), variable);
            default:
                throw new IllegalArgumentException("The type [" + type + "] is not supported");
        }
    }

}
