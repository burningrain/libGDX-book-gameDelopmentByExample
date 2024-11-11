package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;

public interface ComponentListener<T extends Component> {

    void update(T component);

}
