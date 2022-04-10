package com.senpure.cli.test;

import com.beust.jcommander.JCommander;
import com.senpure.cli.*;
import org.junit.jupiter.api.Test;

/**
 * CommandTest
 *
 * @author senpure
 * @time 2020-07-28 10:44:46
 */

public class CommandTest {



    public  void s()
    {

    }
    public RootCommand rootCommand() {


        JCommander rootCommander = new JCommander();
        rootCommander.setProgramName("commandTest");
        rootCommander.addCommand(new GoldCommand());
        rootCommander.addCommand(new MissionCommand());

        HelpCommand helpCommand = new HelpCommand(rootCommander);
        rootCommander.addCommand(helpCommand);
        return new RootCommand(rootCommander, new AnsiConsoleCommandProcess(true), new DefaultCommandSplitter());
    }


    @Test
    public void usageTest() {
        // rootCommander.parse(cmdArray);
        RootCommand rootCommand = rootCommand();
        String command = "help -d";
        System.out.println("详细使用帮助:" + command);

        rootCommand.process(command);

        rootCommand = rootCommand();
        System.out.println("简单使用帮助:help");
        rootCommand.process("help");

        rootCommand = rootCommand();
        System.out.println("单个使用帮助(help):help -h");
        rootCommand.process("help -h");


        rootCommand = rootCommand();
        System.out.println("单个使用帮助(gold):gold -h");
        rootCommand.process("gold -h");


        rootCommand = rootCommand();
        System.out.println("单个使用帮助(mission):mission help");
        rootCommand.process("mission help");


    }

    @Test
    public void completionTest() {
        RootCommand rootCommand = rootCommand();
        System.out.println("基础命令补全:\t");

        rootCommand.process("\t");


        System.out.println("基础命令补全2:go\t");
        rootCommand.process("go\t");


        System.out.println("单个命令补全:gold \t");
        rootCommand.process("gold \t");


        System.out.println("单个命令补全2:gold gol\t");
        rootCommand.process("gold gol\t");


        System.out.println("单个命令补全3:mission -m 10000 -\t");
        rootCommand.process("mission -m 10000 -\t");
    }

    @Test
    public void processTest() {
        RootCommand rootCommand = rootCommand();
        System.out.println("执行单个命令:mission -m 10000");
        rootCommand.process("mission -m 10000");

        rootCommand = rootCommand();
        System.out.println("执行单个命令2:gold -u 10086 -g 500");
        rootCommand.process("gold -u 10086 -g 500");

        rootCommand = rootCommand();
        System.out.println("执行单个命令3:gold -u 10086 -g -500");
        rootCommand.process("gold -u 10086 -g -500");
    }

    @Test
    public  void s2()
    {
        RootCommand rootCommand = rootCommand();
        rootCommand.getCommander().getCommands();
    }
}
