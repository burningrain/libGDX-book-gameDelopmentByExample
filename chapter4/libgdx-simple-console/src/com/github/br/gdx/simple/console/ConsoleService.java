package com.github.br.gdx.simple.console;

import com.github.br.gdx.simple.console.exception.CommandExecutionException;
import com.github.br.gdx.simple.console.exception.CommandNotFoundException;
import com.github.br.gdx.simple.console.exception.ConsoleException;

import java.lang.reflect.Array;
import java.util.HashMap;

/**
 * Created by user on 23.12.2018.
 */
public class ConsoleService {

    private HashMap<String, Command> commands = new HashMap<String, Command>();
    private ConsoleOutput consoleOutput;

    public void addCommands(Command[] commands) {
        for (Command command : commands) {
            this.commands.put(command.getTitle(), command);
        }

        HelpCommand helpCommand = new HelpCommand();
        this.commands.put(helpCommand.getTitle(), helpCommand);
    }

    public void interpretInput(String consoleText) throws ConsoleException {
        String[] strings = consoleText.trim().split(" ");
        String commandTitle = strings[0];
        Command command = commands.get(commandTitle);
        if(command == null) {
            throw new CommandNotFoundException("The command [" + commandTitle + "] is not found");
        }
        try {
            String result = command.execute(copyArray(strings, 1, String.class));
            if(consoleOutput != null) consoleOutput.message(result);
        } catch (Exception e) {
            throw new CommandExecutionException(e);
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

    //todo а если хочется дать консоль и пользователю, а тут такое?!
    private class HelpCommand implements Command {

        @Override
        public String execute(String[] args) throws CommandExecutionException {
            if(args.length == 0) {
                StringBuilder builder = new StringBuilder();
                builder.append("availability commands:").append("\n");
                for (String commandName : commands.keySet()) {
                    builder.append(commandName).append("\n");
                }
                return builder.toString();
            }
            if(args.length == 1) {
                Command command = commands.get(args[0]);
                if(command == null) {
                    throw new IllegalArgumentException("The command [" + args[0] + "] is not found");
                }
                return command.help();
            }
            throw new IllegalArgumentException("Params amount is wrong! Need 1 or 0 params");
        }

        @Override
        public String getTitle() {
            return "help";
        }

        @Override
        public String help() {
            StringBuilder builder = new StringBuilder();
            builder
                    .append("return:").append("\n")
                    .append("1) list of availability commands [without params]").append("\n")
                    .append("2) command info [with params]").append("\n")
                    .append("params:").append("\n")
                    .append("command_name:String [optionally]");
            return builder.toString();
        }

    }


}
