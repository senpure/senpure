package com.senpure.cli;

import com.beust.jcommander.JCommander;

/**
 * CompletionProcess
 *
 * @author senpure
 * @time 2020-07-24 16:33:47
 */
public class CompletionProcess {
    private String[] cmdArray;
    private boolean endSpace;
    private JCommander commander;

    public String[] getCmdArray() {
        return cmdArray;
    }

    public void setCmdArray(String[] cmdArray) {
        this.cmdArray = cmdArray;
    }

    public boolean isEndSpace() {
        return endSpace;
    }

    public void setEndSpace(boolean endSpace) {
        this.endSpace = endSpace;
    }

    public JCommander getCommander() {
        return commander;
    }

    public void setCommander(JCommander commander) {
        this.commander = commander;
    }
}
