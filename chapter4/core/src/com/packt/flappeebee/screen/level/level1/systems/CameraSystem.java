package com.packt.flappeebee.screen.level.level1.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.ViewPortComponent;

@All(ViewPortComponent.class)
public class CameraSystem extends IteratingSystem {

    protected ComponentMapper<TransformComponent> transformMapper;
    protected ComponentMapper<ViewPortComponent> viewportMapper;

    private int focus = -1;
    private final float xMin, xMax, yMin, yMax;

    private Vector3 mVector3 = new Vector3();

    public CameraSystem(float xMin, float xMax, float yMin, float yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    @Override
    protected void process(int entity) {
        ViewPortComponent viewPortComponent = viewportMapper.get(entity);
        Camera camera = viewPortComponent.viewPort.getCamera();

        if (focus != -1) {
            TransformComponent transformComponent = transformMapper.get(focus);

            if (transformComponent != null) {

                float x = Math.max(xMin, Math.min(xMax, transformComponent.x));
                float y = Math.max(yMin, Math.min(yMax, transformComponent.y + 2));

                //camera.position.set(x, y, 0);

                mVector3.set(x, y, 0);
                camera.position.lerp(mVector3, 0.1f);
            }
        }
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }
}
