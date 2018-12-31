package com.github.br.ecs.simple.engine.debug.console;

import com.github.br.ecs.simple.engine.debug.console.exception.CommandNotFoundException;
import com.github.br.ecs.simple.engine.debug.console.exception.ConsoleException;

import java.lang.reflect.Array;
import java.util.HashMap;

/**
 * Created by user on 23.12.2018.
 */
public class ConsoleService {

    private HashMap<String, Command> commands = new HashMap<String, Command>();
    private ConsoleOutput consoleOutput;

    public ConsoleService(Command[] commands) {
        if(commands != null) {
            for (Command command : commands) {
                this.commands.put(command.getTitle(), command);
            }
        }
    }

    public void interpretInput(String consoleText) throws ConsoleException {
        String[] strings = consoleText.split(" ");
        String commandTitle = strings[0];
        Command command = commands.get(commandTitle);
        if(command == null) {
            throw new CommandNotFoundException("The command [" + commandTitle + "] is not found");
        }
        try {
            command.execute(copyArray(strings, 1, String.class));
            if(consoleOutput != null) consoleOutput.message("The command [" + commandTitle + "] successfully executed");
        } catch (NoSuchMethodException e) {
            throw new ConsoleException(e);
        }
    }

    public void setConsoleOutput(ConsoleOutput consoleOutput) {
        this.consoleOutput = consoleOutput;
    }

    // todo перенести в utils
    private static <T> T[] copyArray(T[] source, int beginIdx, Class<T> clazz) throws NoSuchMethodException {
        int length = source.length - beginIdx;
        T[] dest = (T[]) Array.newInstance(clazz, length);
        System.arraycopy(source, beginIdx, dest, 0, length);
        return dest;
    }

    public interface ConsoleOutput {
        void message(String message);
    }

}
