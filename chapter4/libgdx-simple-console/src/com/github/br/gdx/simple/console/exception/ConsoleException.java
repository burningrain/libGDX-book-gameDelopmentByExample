package com.github.br.gdx.simple.console.exception;

/**
 * Created by user on 31.12.2018.
 */
public class ConsoleException extends Exception {

    public ConsoleException() {
        super();
    }

    public ConsoleException(String message) {
        super(message);
    }

    public ConsoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsoleException(Throwable cause) {
        super(cause);
    }

}
