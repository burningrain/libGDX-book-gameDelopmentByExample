package com.github.br.ecs.simple.engine;


import java.lang.reflect.Field;

public final class EcsReflectionHelper {

    private EcsReflectionHelper() {
    }

    //todo избавиться от рефлексии при создании сущности
    public static EcsNode createAndFillNode(Class<EcsNode> nodeClass, EcsEntity entity) {
        EcsNode node = null;
        try {
            node = nodeClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        Field[] fields = nodeClass.getDeclaredFields();
        for (Field field : fields) {
            Class componentClass = field.getType();
            EcsComponent componentObject = entity.getComponent(componentClass);
            if (componentObject != null) {
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
        node.entityId = entity.getId();
        return node;
    }

}
