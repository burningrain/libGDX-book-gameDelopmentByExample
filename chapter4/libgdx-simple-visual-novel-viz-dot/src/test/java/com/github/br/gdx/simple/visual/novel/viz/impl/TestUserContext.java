package com.github.br.gdx.simple.visual.novel.viz.impl;

import com.github.br.gdx.simple.visual.novel.api.ElementId;

public class TestUserContext implements TestInterfaceUserContext {

    public ElementId nextId;


    @Override
    public void setNextId(ElementId elementId) {
        this.nextId = elementId;
    }

    @Override
    public ElementId getNextId() {
        return nextId;
    }

}
