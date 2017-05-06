package com.github.br.ecs.simple;


import com.github.br.ecs.simple.component.EcsComponent;
import com.github.br.ecs.simple.node.EcsNode;

import java.lang.reflect.Field;

public final class EcsReflectionHelper {

    private EcsReflectionHelper(){}

    public static EcsNode createAndFillNode(Class<EcsNode> nodeClass, EcsEntity entity){
        EcsNode node = null;
        try {
            node = nodeClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Field[] fields = nodeClass.getDeclaredFields();
        for(Field field : fields){
            Class componentClass = field.getType();
            EcsComponent componentObject = entity.getComponent(componentClass);
            if(componentObject != null){
                field.setAccessible(true);
                try {
                    field.set(node, componentObject);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                // необходимые компоненты для обработки системой не найдены.
                // возвращаем null, а в контейнере проверка, чтоб null не пихался в обработку системой
                return null;
            }
        }
        return node;
    }

    public static <T> T getValue(Object object, String fieldName){
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T)field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setValue(Object object, String fieldName, Object value){
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
