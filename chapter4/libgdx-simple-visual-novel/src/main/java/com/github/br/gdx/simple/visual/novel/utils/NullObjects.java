package com.github.br.gdx.simple.visual.novel.utils;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;

public interface NullObjects {

    ElementId THIS_IS_END_ELEMENT_IN_THE_SCENE = ElementId.of("=-9999-=");

    CurrentState UP_TO_PARENT_PROCESS = CurrentState.of(ElementId.of("=-UP-="), ElementId.of("=-UP-="));

    CurrentState DOWN_INTO_SUB_PROCESS = CurrentState.of(ElementId.of("=-DOWN-="), ElementId.of("=-DOWN-="));

}
