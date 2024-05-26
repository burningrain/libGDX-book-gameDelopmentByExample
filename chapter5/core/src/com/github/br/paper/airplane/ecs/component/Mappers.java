package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.ComponentMapper;

public class Mappers {

    public final ComponentMapper<InitComponent> initMapper = ComponentMapper.getFor(InitComponent.class);
    public final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    public final ComponentMapper<Box2dComponent> box2dMapper = ComponentMapper.getFor(Box2dComponent.class);
    public final ComponentMapper<TextureComponent> textureMapper = ComponentMapper.getFor(TextureComponent.class);

    public final ComponentMapper<DeleteComponent> deleteMapper = ComponentMapper.getFor(DeleteComponent.class);
    public final ComponentMapper<ScriptComponent> scriptMapper = ComponentMapper.getFor(ScriptComponent.class);

    public final ComponentMapper<HeroComponent> heroMapper = ComponentMapper.getFor(HeroComponent.class);

}
