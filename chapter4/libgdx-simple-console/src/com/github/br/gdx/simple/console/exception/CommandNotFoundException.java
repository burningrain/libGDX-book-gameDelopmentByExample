package com.github.br.gdx.simple.console.exception;

/**
 * Created by user on 31.12.2018.
 */
public class CommandNotFoundException extends ConsoleException {

    public CommandNotFoundException(String message) {
        super(message);
    }

    public CommandNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandNotFoundException(Throwable cause) {
        super(cause);
    }

}
