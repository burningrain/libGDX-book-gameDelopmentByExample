package com.github.br.ecs.simple.engine.debug.console;

import com.github.br.ecs.simple.engine.debug.console.exception.CommandExecutionException;

/**
 * Created by user on 23.12.2018.
 */
public interface Command {

    void execute(String[] args) throws CommandExecutionException;
    String getTitle();

}
