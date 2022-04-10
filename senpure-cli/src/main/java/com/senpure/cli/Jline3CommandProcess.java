package com.senpure.cli;

import org.jline.reader.Candidate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Jline3CommandProcess extends AnsiConsoleCommandProcess {


    public static final ThreadLocal<List<Candidate>> CANDIDATES_THREAD_LOCAL = ThreadLocal.withInitial(ArrayList::new);

    public Jline3CommandProcess() {
    }

    public Jline3CommandProcess(boolean enable) {
        super(enable);
    }

    @Override
    public void completionOptions(@Nonnull List<Option> options) {
        List<Candidate> candidates = CANDIDATES_THREAD_LOCAL.get();
        int sort=0;
        for (Option option : options) {

            candidates.add(new Candidate(option.getValue(), option.getDisplay(), option.getGroup(),option.getDescription() , option.getSuffix(), option.getKey(), option.isComplete(), ++sort));
        }
    }

    @Override
    public void completion(String command, boolean fromMultiple) {
        throw new RuntimeException("设置rootCommand.setFullCompletion(false)");

    }
}
