package com.github.br.gdx.simple.visual.novel.api.context;

import com.github.br.gdx.simple.visual.novel.api.ElementId;

public class CurrentState {

    public ElementId sceneId; // nullable because the first scene
    public ElementId nodeId;  // nullable because the first state
    public CurrentState parentState; // nullable

    @Override
    public String toString() {
        return "CurrentState{" +
                "sceneId=" + sceneId +
                ", nodeId=" + nodeId +
                ", parentState=" + parentState +
                '}';
    }

}
