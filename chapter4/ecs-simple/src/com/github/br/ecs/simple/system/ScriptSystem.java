package com.github.br.ecs.simple.system;


import com.github.br.ecs.simple.EcsScript;
import com.github.br.ecs.simple.node.ScriptNode;

public class ScriptSystem extends EcsSystem<ScriptNode> {

    public ScriptSystem() {
        super(ScriptNode.class);
    }

    @Override
    public void update(float delta) {
        for(ScriptNode node : nodes){
            for(EcsScript script : node.scriptComponent.scripts){
                script.update(delta);
            }
        }
    }
}
