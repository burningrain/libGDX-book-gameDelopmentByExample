package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;
import com.github.br.paper.airplane.bullet.BulletType;

public class BulletTypeComponent implements Component {

    public BulletType bulletType;

    public BulletTypeComponent(BulletType bulletType) {
        this.bulletType = bulletType;
    }

}
