package com.github.br.paper.airplane.ecs.system.hero;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.ecs.component.Mappers;

public interface HeroSybSystem {

    void processHero(
            Engine engine,
            GameManager gameManager,
            Mappers mappers,
            Entity entity
    );

}
