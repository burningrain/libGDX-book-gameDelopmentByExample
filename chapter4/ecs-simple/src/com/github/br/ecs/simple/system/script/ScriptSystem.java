package com.github.br.ecs.simple.system.script;


import com.github.br.ecs.simple.engine.debug.DebugDataContainer;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.engine.EcsSystem;

import java.util.Collection;

public class ScriptSystem extends EcsSystem<ScriptNode> {

    public ScriptSystem() {
        super(ScriptNode.class);
    }

    @Override
    public void update(float delta, Collection<ScriptNode> nodes) {
        for(ScriptNode node : nodes){
            for(EcsScript script : node.scriptComponent.scripts){
                script.update(delta);
            }
        }
    }

    @Override
    public DebugDataContainer getDebugData() {
        return new DebugDataContainer();
    }
}
