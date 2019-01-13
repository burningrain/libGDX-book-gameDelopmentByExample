package com.github.br.ecs.simple.engine;

/**
 * Маркерный интерфейс для ноды системы.
 * Нода содержит ссылки на компоненты сущности, необходимые системе.
 * Система работает с нодами, не с сущностями.
 */
public abstract class EcsNode {

    public int entityId;

}
