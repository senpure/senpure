package com.senpure.cli;

import com.beust.jcommander.JCommander;

import javax.annotation.Nonnull;

public abstract class AbstractConsoleCommandApplication extends AbstractCommandApplication {

    public static final String PROPERTY_COMPLETE_CONSOLE = "complete.console";

    protected abstract JCommander rootCommander();

    private boolean completeConsole = false;

    public AbstractConsoleCommandApplication() {
        completeConsole = completeConsole();
    }

    @Override
    protected RootCommand rootCommand() {
        JCommander rootCommander = rootCommander();
        if (completeConsole) {
            return new RootCommand(rootCommander, new Jline3CommandProcess(true), new DefaultCommandSplitter());
        } else {
            return new RootCommand(rootCommander, new AnsiConsoleCommandProcess(true), new DefaultCommandSplitter());
        }
    }

    @Override
    public void start() {
        CommandApplication commandApplication;
        if (completeConsole) {
            commandApplication=new AbstractJline3CommandApplication() {
                @Nonnull
                @Override
                protected RootCommand rootCommand() {
                    return new RootCommand(rootCommander(), new Jline3CommandProcess(true), new DefaultCommandSplitter());
                }

            };
        }
        else {
        commandApplication=new AbstractIncompleteConsoleCommandApplication() {
            @Nonnull
            @Override
            protected RootCommand rootCommand() {
                return new RootCommand(rootCommander(), new AnsiConsoleCommandProcess(true), new DefaultCommandSplitter());

            }
        };
        }
        commandApplication.start();
    }

    private static boolean completeConsole() {
        if (getBoolean()) {
            return true;
        }
        return System.console() != null;
    }

    public static boolean getBoolean() {
        String value = System.getProperties().getProperty(PROPERTY_COMPLETE_CONSOLE);
        if (value == null) {
            return false;
        }
        value = value.trim().toLowerCase();
        if (value.isEmpty()) {
            return true;
        }
        if ("true".equals(value) || "yes".equals(value) || "1".equals(value)) {
            return true;
        }
        return false;
    }
}
