package com.github.br.ecs.simple.engine.debug;

import com.github.br.ecs.simple.engine.debug.data.*;
import com.github.br.ecs.simple.engine.debug.drawobject.*;

import java.util.HashMap;

/**
 * Created by user on 07.04.2018.
 */
public class DebugRendererObjectFactory {

    private static HashMap<Class<? extends DebugData>, DebugDrawObject> map =
            new HashMap<Class<? extends DebugData>, DebugDrawObject>();

    static {
        map.put(CircleData.class, new CircleDrawObject());
        map.put(RectangleData.class, new RectangleDrawObject());
        map.put(PointData.class, new PointDrawObject());
        map.put(TableData.class, new TableDrawObject());
    }

    public static DebugDrawObject getDebugDrawObject(Class<? extends DebugData> clazz) {
        DebugDrawObject debugDrawObject = map.get(clazz);
        if(debugDrawObject == null) {
            throw new IllegalArgumentException(String.format("неизвестный объект для отобрадения в дебагере %s",
                    clazz));
        }
        return debugDrawObject;
    }


}
