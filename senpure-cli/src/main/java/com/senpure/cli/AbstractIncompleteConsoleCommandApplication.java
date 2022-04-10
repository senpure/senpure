package com.senpure.cli;

import com.beust.jcommander.JCommander;

import java.io.*;

public abstract class AbstractIncompleteConsoleCommandApplication extends AbstractCommandApplication {




    @Override
    public void start() {

        RootCommand rootCommand = rootCommand();
        JCommander rootCommander = rootCommand.getRootCommander();

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FileDescriptor.in)));
        String prompt = rootCommander.getProgramName();
        prompt = prompt == null ? "command>" : prompt + ">";
        prompt = rootCommand.getCommandProcess().color(Color.GREEN, prompt) + " ";

        boolean flag = true;
        rootCommand.getCommandProcess().warn("推测为 ide 控制台 只有简单执行命令功能");
        rootCommand.getCommandProcess().info("如需自动补全|回显|历史等功能请打包后执行");
        rootCommand.getCommandProcess().info("如明确该控制台可使用完整功能请使用-Dcomplete.console=true");
        rootCommand.getCommandProcess().info("如需帮助请输入help 或 help -d");
        rootCommand.getCommandProcess().info("请输入命令开始执行");
        while (flag) {
            try {
                System.out.print(prompt);
                String line = br.readLine();
                //ctrl + D
                if (line == null) {
                    break;
                }
                if (line.isEmpty()) {
                    continue;
                }

                if (line.endsWith(rootCommand.getCompletionChar())) {
                    rootCommand().getCommandProcess().info("补全查询:" + line);

                } else {
                    rootCommand().getCommandProcess().info("命令执行:" + line);
                }
                rootCommand().process(line);
            } catch (Exception e) {
               // e.printStackTrace();
                rootCommand.getCommandProcess().error(e.getMessage());
                flag = false;
            }
        }
        rootCommand.getCommandProcess().success("命令控制台结束");


    }

}
