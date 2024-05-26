package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.br.paper.airplane.ecs.component.Script;
import com.github.br.paper.airplane.ecs.component.ScriptComponent;

public class ScriptSystem extends IteratingSystem {

    private final ComponentMapper<ScriptComponent> scriptMapper = ComponentMapper.getFor(ScriptComponent.class);

    public ScriptSystem() {
        super(Family.all(ScriptComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ScriptComponent scriptComponent = scriptMapper.get(entity);
        if (scriptComponent.scripts != null) {
            for (Script script : scriptComponent.scripts) {
                script.update(entity, deltaTime);
            }
        }
    }

}
