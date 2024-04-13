package com.github.br.gdx.simple.visual.novel.viz.impl;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public interface TestInterfaceUserContext extends UserContext {

     void setNextId(ElementId elementId);

    ElementId getNextId();

}
