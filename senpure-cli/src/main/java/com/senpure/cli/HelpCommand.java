package com.senpure.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.UsageFormatter;

/**
 * HelpCommand
 *
 * @author senpure
 * @time 2020-07-23 11:59:55
 */
@Parameters(commandNames = {"help"}, commandDescription = "使用帮助")
public class HelpCommand extends AbstractHelpableCommand {
    @Parameter(names = {"-d", "detail"})
    private boolean detail;

    private final JCommander rootCommander;

    public HelpCommand(JCommander rootCommander) {
        this.rootCommander = rootCommander;
    }

    @Override
    public void process0(CommandProcess process) {
        StringBuilder out = new StringBuilder();
        if (detail) {
            UsageFormatter formatter = new UsageFormatter(rootCommander);
            formatter.usage(out);

        } else {
            UsageFormatter usageFormatter = new UsageFormatter(rootCommander);
            String programName = rootCommander.getProgramDisplayName() != null
                    ? rootCommander.getProgramDisplayName() : "<main class>";

            out.append(programName)
                    //.append(" ")
                   // .append("Commands:")
            ;
            usageFormatter.appendCommands(out,"",false);
        }
        process.feed(out.toString());

    }

    public boolean isDetail() {
        return detail;
    }

    public void setDetail(boolean detail) {
        this.detail = detail;
    }

}
