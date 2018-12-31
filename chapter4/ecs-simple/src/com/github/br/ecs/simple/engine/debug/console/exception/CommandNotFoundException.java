package com.github.br.ecs.simple.engine.debug.console.exception;

/**
 * Created by user on 31.12.2018.
 */
public class CommandNotFoundException extends ConsoleException {

    public CommandNotFoundException() {
        super();
    }

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
