package com.github.br.gdx.simple.visual.novel.utils;

import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;

public class StateStack extends ArrayStack<CurrentState> {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        boolean isFirst = true;
        for (CurrentState currentState : getArrayList()) {
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append("/");
            }
            builder.append(currentState.sceneId).append(".").append(currentState.nodeId);
        }
        return builder.toString();
    }

}
