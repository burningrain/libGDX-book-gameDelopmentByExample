package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.ComponentMapper;
import com.github.br.paper.airplane.ecs.component.render.RenderComponent;

public class Mappers {

    public final ComponentMapper<InputComponent> inputMapper = ComponentMapper.getFor(InputComponent.class);

    public final ComponentMapper<InitComponent> initMapper = ComponentMapper.getFor(InitComponent.class);
    public final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    public final ComponentMapper<Box2dComponent> box2dMapper = ComponentMapper.getFor(Box2dComponent.class);
    public final ComponentMapper<RenderComponent> renderMapper = ComponentMapper.getFor(RenderComponent.class);

    public final ComponentMapper<DestroyedComponent> destroyMapper = ComponentMapper.getFor(DestroyedComponent.class);
    public final ComponentMapper<ScriptComponent> scriptMapper = ComponentMapper.getFor(ScriptComponent.class);

    public final ComponentMapper<HeroComponent> heroMapper = ComponentMapper.getFor(HeroComponent.class);
    public final ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);

    public final ComponentMapper<BulletTypeComponent> bulletTypeMapper = ComponentMapper.getFor(BulletTypeComponent.class);
    public final ComponentMapper<DelayComponent> delayMapper = ComponentMapper.getFor(DelayComponent.class);

    public final ComponentMapper<SimpleTweenComponent> simpleTweenMapper = ComponentMapper.getFor(SimpleTweenComponent.class);
}
