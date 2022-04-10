package com.senpure.cli.test;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.senpure.cli.AbstractCommand;
import com.senpure.cli.CommandProcess;
import com.senpure.cli.AbstractHelpableCommand;

/**
 * MissionCommand
 *
 * @author senpure
 * @time 2020-07-28 10:42:03
 */
@Parameters(commandNames = {"mission"}, commandDescription = "任务操作")
public class MissionCommand extends AbstractHelpableCommand {
    @Parameter(names = {"-u", "user"}, description = "玩家id(0表示自己)")
    private int playerId;
    @Parameter(names = {"-m", "mission"}, description = "完成的任务id")
    private int missionId;


    @Override
    public void process0(CommandProcess process) {

        process.feed("[" + playerId + "] mission [" + missionId + "] success");
    }
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }


}
