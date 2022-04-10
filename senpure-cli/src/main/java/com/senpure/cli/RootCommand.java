package com.senpure.cli;

import com.beust.jcommander.JCommander;

import java.util.List;

/**
 * RootCommand
 *
 * @author senpure
 * @time 2020-07-28 10:59:28
 */
public class RootCommand extends AbstractCommand {
    public static final String DEFAULT_COMPLETION_CHAR = "\t";
    public static final ThreadLocal<Command> COMMAND_THREAD_LOCAL = new ThreadLocal<>();
    private final JCommander rootCommander;

    private final CommandProcess commandProcess;

    private final CommandSplitter commandSplitter;


    private String completionChar = DEFAULT_COMPLETION_CHAR;
    /**
     * 当补全只有一个选项时是否修饰为一个完整的命令
     */
    private boolean fullCompletion = false;
    private String command;

    public RootCommand(JCommander rootCommander) {
        this(rootCommander, new DefaultCommandProcess(), new DefaultCommandSplitter());
    }

    public RootCommand(JCommander rootCommander, CommandProcess commandProcess, CommandSplitter commandSplitter) {
        this.rootCommander = rootCommander;
        this.commandProcess = commandProcess;
        this.commandSplitter = commandSplitter;

    }

    public void process(String command) {
//        if (command == null || command.trim().isEmpty()) {
//            return;
//        }
        if (command == null || command.isEmpty()) {
            return;
        }
        this.command = command;
        try {
            COMMAND_PROCESS_THREAD_LOCAL.set(commandProcess);
            if (command.endsWith(completionChar)) {
                do {
                    command = command.substring(0, command.length() - completionChar.length());
                }
                while (command.endsWith(completionChar));
                String[] cmdArray = commandSplitter.split(command);
                completion(command, cmdArray);

            } else {
                String[] cmdArray = commandSplitter.split(command);
                rootCommander.parse(cmdArray);
                process(commandProcess);
            }

        } catch (Exception e) {
            String message = e.toString();
            commandProcess.feed(color(Color.RED, message));
        } finally {
            COMMAND_PROCESS_THREAD_LOCAL.remove();
        }
    }

    public void completion(String command) {
        String[] cmdArray = commandSplitter.split(command);
        completion(command, cmdArray);
    }

    private void completion(String command, String[] cmdArray) {
        List<Option> options = CompletionUtil.completion(rootCommander, command, cmdArray);
        int size = options.size();
        if (fullCompletion) {
            if (size == 0) {
                commandProcess.completionOptions(options);
            } else {
                int endSize = command.length();
                if (!command.endsWith(" ") && cmdArray.length > 0) {
                    String end = cmdArray[cmdArray.length - 1];
                    endSize = end.length();
                    int index = command.lastIndexOf(end);
                    if (index > -1) {
                        command = command.substring(0, index);
                    }
                }
                StringBuilder sb = new StringBuilder();
                int minLen = 9999;
                for (Option option : options) {
                    minLen = Math.min(minLen, option.getValue().length());
                }
                String first = options.get(0).getValue();
                for (int i = 0; i < minLen; i++) {
                    boolean flag = true;
                    char c = first.charAt(i);
                    for (int j = 1; j < size; j++) {
                        char inChar = options.get(j).getValue().charAt(i);
                        if (inChar != c) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        sb.append(c);
                    }
                }
                if (sb.length() > endSize) {
                    command += sb.toString();
                    commandProcess.completion(command, size > 1);
                } else {
                    commandProcess.completionOptions(options);
                }
            }

        } else {
            commandProcess.completionOptions(options);
        }
    }

    public void process(CommandProcess process) {
        String command = rootCommander.getParsedCommand();
        if (command != null) {
            JCommander jCommander = rootCommander.getCommands().get(command);
            process(jCommander, process);
        } else {
            process.feed("无效命令[" + this.command + "]");
            // process(commander, process);
        }
    }


    private void process(JCommander commander, CommandProcess process) {
        Object obj = commander.getObjects().get(0);
        if (obj == null) {
            return;
        }
        if (obj instanceof Command) {
            Command cmd = (Command) obj;

            cmd.process(process);
            if (cmd.running()) {
                COMMAND_THREAD_LOCAL.set(cmd);
            }

        }
    }

    public void setFullCompletion(boolean fullCompletion) {
        this.fullCompletion = fullCompletion;
    }

    public JCommander getRootCommander() {
        return rootCommander;
    }

    public String getCompletionChar() {
        return completionChar;
    }

    public void setCompletionChar(String completionChar) {
        this.completionChar = completionChar;
    }

    public CommandProcess getCommandProcess() {
        return commandProcess;
    }

    public CommandSplitter getCommandSplitter() {
        return commandSplitter;
    }
}
