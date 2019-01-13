package com.github.br.ecs.simple.system.script;


import com.badlogic.gdx.utils.IntMap;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.engine.EcsSystem;

import java.util.Collection;

public class ScriptSystem extends EcsSystem<ScriptNode> {

    public ScriptSystem() {
        super(ScriptNode.class);
    }

    @Override
    public void update(float delta, IntMap.Values<ScriptNode> nodes) {
        for(ScriptNode node : nodes){
            for(EcsScript script : node.scriptComponent.scripts){
                script.update(delta);
            }
        }
    }

}
