package com.github.br.gdx.simple.visual.novel.api.context;

import com.github.br.gdx.simple.visual.novel.api.ElementId;

public class CurrentState {

    public ElementId sceneId; // nullable because the first scene
    public ElementId nodeId;  // nullable because the first state

    private CurrentState() {
    }

    public static CurrentState of(ElementId sceneId, ElementId nodeId) {
        CurrentState currentState = new CurrentState();
        currentState.sceneId = sceneId;
        currentState.nodeId = nodeId;

        return currentState;
    }

    public CurrentState copy() {
        CurrentState result = new CurrentState();
        result.sceneId = this.sceneId;
        result.nodeId = this.nodeId;
        return result;
    }

    @Override
    public String toString() {
        return "sceneId=[" + sceneId + "], nodeId=[" + nodeId + "]";
    }

}
