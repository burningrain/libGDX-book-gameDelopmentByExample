package com.packt.snake.model.utils;

/**
 * Created by user on 27.02.2017.
 */
public class Vector {

    public int x, y;

    public Vector(){
        this.x = 0;
        this.y = 0;
    }

    public Vector(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (x != vector.x) return false;
        return y == vector.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
