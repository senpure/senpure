package com.senpure.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.UsageFormatter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * AbsCommand
 *
 * @author senpure
 * @time 2020-07-28 10:23:28
 */
public abstract class AbstractCommand implements Command {
    protected JCommander commander;
    protected UsageFormatter usageFormatter;
    protected static Executor executor;

    public AbstractCommand() {
        commander = new JCommander(this);
        Parameters parameters = getClass().getAnnotation(Parameters.class);
        String name = "";
        if (parameters != null) {

            for (String commandName : parameters.commandNames()) {
                if (commandName.length() > name.length()) {
                    name = commandName;
                }
            }

        }
        if (name.length() == 0) {
            name = getClass().getSimpleName();
            if (name.endsWith("Command")) {
                name = name.substring(0, name.length() - 7);
            }
        }
        commander.setProgramName(name);

        usageFormatter = new UsageFormatter(commander);
    }

    @Override
    public String usage() {
        StringBuilder out = new StringBuilder();
        usageFormatter.usage(out);
        return out.toString();
    }

    protected String color(Color color, String element) {
        return Command.COMMAND_PROCESS_THREAD_LOCAL.get().color(color, element);
    }

    protected String color(Color color, int element) {
        return Command.COMMAND_PROCESS_THREAD_LOCAL.get().color(color, element);
    }

    protected String color(Color color, long element) {
        return Command.COMMAND_PROCESS_THREAD_LOCAL.get().color(color, element);
    }

    protected void background(Runnable runnable) {
        if (executor == null) {
            executor = Executors.newSingleThreadScheduledExecutor(r -> {

                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("command-background-thread");
                return thread;
            });
        }

        if (runnable instanceof CommandProcessRunnable) {
            executor.execute(runnable);
        } else {
            executor.execute(new CommandProcessRunnable(runnable));

        }


    }

    public JCommander getCommander() {
        return commander;
    }

    private static class CommandProcessRunnable implements Runnable {

        private final CommandProcess commandProcess;

        private final Runnable runnable;

        public CommandProcessRunnable(Runnable runnable) {
            this.runnable = runnable;
            commandProcess = COMMAND_PROCESS_THREAD_LOCAL.get();
        }

        @Override
        public void run() {
            COMMAND_PROCESS_THREAD_LOCAL.set(commandProcess);
            try {
                runnable.run();
            } finally {
                COMMAND_PROCESS_THREAD_LOCAL.remove();
            }
        }
    }
}
