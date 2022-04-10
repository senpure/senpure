package com.senpure.cli;

import com.beust.jcommander.JCommander;
import org.jline.reader.*;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

public abstract class AbstractJline3CommandApplication extends AbstractCommandApplication {

    public void start() {
        RootCommand rootCommand = rootCommand();
        JCommander rootCommander = rootCommand.getRootCommander();
        try {
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            terminal.puts(InfoCmp.Capability.clear_screen);
            rootCommand.getCommandProcess().info("命令行接口已经准备就绪！");
            rootCommand.getCommandProcess().info("输入 help 或 help -d 查看使用帮助");
            rootCommand.getCommandProcess().info("使用 tab  键进行命令补全");
            rootCommand.getCommandProcess().info("使用 ↑↓ 键查看命令历史");
            rootCommand.getCommandProcess().info("使用 ctrl + (shift) + l  清屏");
            rootCommand.getCommandProcess().info("使用 ctrl + c  退出|暂停");

            String prompt = rootCommander.getProgramName();
            prompt = prompt == null ? "command>" : prompt + ">";
            prompt = rootCommand.getCommandProcess().color(Color.GREEN, prompt) + " ";

            Completer completer = (reader, line, candidates) -> {
                Command runningCommand = RootCommand.COMMAND_THREAD_LOCAL.get();
                if (runningCommand != null && runningCommand.running()) {
                    return;
                }
                RootCommand command = rootCommand();
                Jline3CommandProcess.CANDIDATES_THREAD_LOCAL.set(candidates);
                command.setFullCompletion(false);
                command.completion(line.line());
            };

            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(completer)
                    .build();
            while (true) {
                String line;
                try {
                    Command runningCommand = RootCommand.COMMAND_THREAD_LOCAL.get();
                    if (runningCommand == null || !runningCommand.running()) {
                        line = lineReader.readLine(prompt);
                        RootCommand command = rootCommand();
                        command.process(line);
                    } else {
                        lineReader.readLine();
                    }

                } catch (UserInterruptException e) {
                    Command command = RootCommand.COMMAND_THREAD_LOCAL.get();
                    if (command != null && command.running()) {
                        command.stop();
                        //System.out.print("\033[1A\033[K");
                        rootCommand.getCommandProcess().feed("\033[2A");
                        //terminal.flush();
                    } else {
                        rootCommand.getCommandProcess().success("thanks");
                        break;
                    }
                } catch (EndOfFileException e) {

                    rootCommand.getCommandProcess().success("thanks");
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
