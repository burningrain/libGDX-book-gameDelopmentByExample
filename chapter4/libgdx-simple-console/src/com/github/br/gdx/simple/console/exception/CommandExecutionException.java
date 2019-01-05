package com.github.br.gdx.simple.console.exception;

/**
 * Created by user on 31.12.2018.
 */
public class CommandExecutionException extends ConsoleException {

    public CommandExecutionException() {
        super();
    }

    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandExecutionException(Throwable cause) {
        super(cause);
    }

}
