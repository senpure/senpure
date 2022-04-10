package com.senpure.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.UsageFormatter;

import java.util.List;

/**
 * Command
 *
 * @author senpure
 * @time 2020-07-23 12:07:02
 */
public interface Command {
    ThreadLocal<CommandProcess> COMMAND_PROCESS_THREAD_LOCAL = ThreadLocal.withInitial(DefaultCommandProcess::new);

    void process(CommandProcess process);

    default List<Option> complete(CompletionProcess process) {
        return CompletionUtil.completionOptions(process);
    }

    default String usage() {
        StringBuilder out = new StringBuilder();
        JCommander jCommander = new JCommander();
        jCommander.addObject(this);
        UsageFormatter formatter = new UsageFormatter(jCommander);
        formatter.usage(out);
        return out.toString();
    }

    default void stop() {
    }

    default boolean running() {
        return false;
    }
}
