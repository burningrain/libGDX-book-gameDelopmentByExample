package com.github.br.ecs.simple.system.script;


import com.badlogic.gdx.utils.IntMap;
import com.github.br.ecs.simple.engine.EcsEntity;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.engine.EcsSystem;

public class ScriptSystem extends EcsSystem {

    public ScriptSystem() {
        super(ScriptComponent.class);
    }

    @Override
    public void update(float delta, IntMap.Values<EcsEntity> entities) {
        for (EcsEntity entity : entities) {
            for (EcsScript script : entity.getComponent(ScriptComponent.class).scripts) {
                script.update(delta);
            }
        }
    }

    @Override
    public void removeEntity(int entityId) {
        EcsEntity entity = entities.remove(entityId);
        for (EcsScript script : entity.getComponent(ScriptComponent.class).scripts) {
            script.dispose();
        }
    }

}
