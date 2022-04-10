package com.senpure.cli.test;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.senpure.cli.Color;
import com.senpure.cli.CommandProcess;
import com.senpure.cli.AbstractHelpableCommand;

/**
 * GoldCommand
 *
 * @author senpure
 * @time 2020-07-28 10:36:53
 */
@Parameters(commandNames = "gold", commandDescription = "金币操作")
public class GoldCommand extends AbstractHelpableCommand {
    @Parameter(names = {"-g", "gold"},order = 1,description = "金币增加/减少操作", required = true)
    private int gold;
    @Parameter(names = {"-u", "user"},order = 2,description = "玩家id(0表示自己)")
    private int playerId;

    @Override
    public void process0(CommandProcess process) {

        process.feed("[" + color(Color.CYAN,playerId ) + "] gold [" + gold + "] success");

    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
