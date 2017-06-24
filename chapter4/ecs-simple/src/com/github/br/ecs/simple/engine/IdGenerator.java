package com.github.br.ecs.simple.engine;

/**
 * Created by user on 28.05.2017.
 */
class IdGenerator {

    private int id = 0;


    public int nextId(){
        return id++;
    }


}
