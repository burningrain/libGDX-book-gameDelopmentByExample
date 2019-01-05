package com.github.br.gdx.simple.console;

import com.github.br.gdx.simple.console.exception.CommandExecutionException;

/**
 * Created by user on 23.12.2018.
 */
public interface Command {

    String execute(String[] args) throws CommandExecutionException;

    String getTitle();

    String help();

}
