package com.senpure.cli;

import com.beust.jcommander.Parameter;

/**
 * AbstractHelpableCommand
 *
 * @author senpure
 * @time 2020-07-28 10:27:05
 */
public abstract class AbstractHelpableCommand extends AbstractCommand {
    @Parameter(names = {"-h", "help"}, description = "使用帮助", order = 1000, help = true)
    private boolean help;

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    @Override
    public void process(CommandProcess process) {
        if (isHelp()) {
            process.feed(usage());
        } else {
            process0(process);
        }
    }

    protected abstract void process0(CommandProcess process);
}
