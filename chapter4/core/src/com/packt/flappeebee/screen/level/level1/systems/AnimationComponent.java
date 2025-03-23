package com.packt.flappeebee.screen.level.level1.systems;

import com.artemis.Component;
import com.github.br.gdx.simple.animation.component.SimpleAnimationComponent;

public class AnimationComponent extends Component {

    public SimpleAnimationComponent simpleAnimationComponent;

    public AnimationComponent(SimpleAnimationComponent simpleAnimationComponent) {
        this.simpleAnimationComponent = simpleAnimationComponent;
    }

    public AnimationComponent(){}

}
