package com.github.br.ecs.simple.engine;

/**
 * Created by user on 28.05.2017.
 */
public class EntityId {

    private int id;
    private String type;

    EntityId(int id, String type){
        this.id = id;
        this.type = type;
    }

    public static EntityId of(int id, String type){
        return new EntityId(id, type);
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityId entityId = (EntityId) o;

        if (id != entityId.id) return false;
        return !(type != null ? !type.equals(entityId.type) : entityId.type != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
