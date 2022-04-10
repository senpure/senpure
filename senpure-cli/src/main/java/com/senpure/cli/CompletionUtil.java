package com.senpure.cli;

import com.beust.jcommander.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * CompletionUtil
 *
 * @author senpure
 * @time 2020-07-24 12:15:07
 */
public class CompletionUtil {
    //private static final ThreadLocal<Boolean> objectFlag = ThreadLocal.withInitial(() -> false);






    @Nonnull
    public static List<Option> completion(JCommander commander, String command, String[] cmdArray) {
        boolean endSpace = command.endsWith(" ");
        CompletionProcess process = new CompletionProcess();
        process.setCommander(commander);
        process.setCmdArray(cmdArray);
        process.setEndSpace(endSpace);
        return completion(process);
    }

    @Nonnull
    public static List<Option> completion(CompletionProcess process) {
        JCommander commander = process.getCommander();
        Map<String, JCommander> commanderMap = commander.getCommands();
        if (commanderMap.size() > 0) {
            List<Option> completions = new ArrayList<>();
            String[] cmdArray = process.getCmdArray();
            switch (cmdArray.length) {
                case 0:
                    completions.addAll(completionCommand(process, ""));
                    completions.addAll(_completionOptions(process));
                    return completions;
                case 1:
                    completions.addAll(completionCommand(process, cmdArray[0]));
                    if (completions.isEmpty()) {
                        completions.addAll(_completionOptions(process));
                    }
                    return completions;
                default:
                    process = cutCommandName(process, cmdArray[0]);
                    if (process != null) {
                        return _completionOptions(process);
                    }
                    return completions;
            }

        } else {
            return _completionOptions(process);
        }

    }


    @Nullable
    private static CompletionProcess cutCommandName(CompletionProcess process, String commandName) {
        JCommander jCommander = process.getCommander().findCommandByAlias(commandName);
        if (jCommander != null) {
            String[] _cmdArray = Arrays.copyOfRange(process.getCmdArray(), 1, process.getCmdArray().length);
            CompletionProcess _process = new CompletionProcess();
            _process.setEndSpace(process.isEndSpace());
            _process.setCmdArray(_cmdArray);
            _process.setCommander(jCommander);
            return _process;
        }
        return null;
    }


    /**
     * 默认补全实现，只支持成对出现的格式
     *
     * @param process
     * @return
     */
    @Nonnull
    public static List<Option> completionOptions(CompletionProcess process) {
        JCommander commander = process.getCommander();

        String[] cmdArray = process.getCmdArray();
        String option = "";
        if (cmdArray.length > 0) {
            if ((cmdArray.length & 1) == 0) {
                if (!process.isEndSpace()) {
                    return Collections.emptyList();
                }
            } else {
                if (!process.isEndSpace()) {
                    option = cmdArray[cmdArray.length - 1];
                } else {
                    return Collections.emptyList();
                }
            }
        }
        Set<String> inputtedOptions = new HashSet<>();
        for (int i = 0; i < cmdArray.length; i += 2) {
            inputtedOptions.add(cmdArray[i]);
        }

        return completionOptions(commander, option, inputtedOptions);
    }

    /**
     *
     * @param commander {@link  com.beust.jcommander}
     * @param option 当前的命令
     * @param inputtedOptions 已经输入过的命令选项
     * @return
     */
    @Nonnull
    private static List<Option> completionOptions(JCommander commander, String option, Set<String> inputtedOptions) {
        List<Option> likes = new ArrayList<>();
        for (ParameterDescription pd : commander.getFields().values()) {
            WrappedParameter parameter = pd.getParameter();
            List<Option> parameterLikes = new ArrayList<>();
            //同一个命令已经有一个别名就不在提示补全了。
            for (String name : parameter.names()) {
                if (inputtedOptions.contains(name)) {
                    parameterLikes.clear();
                    break;
                }
                if (name.equals(option)) {
                    parameterLikes.clear();
                    break;
                }

                if (name.startsWith(option)) {
                   //String group= pd.getParameterized().getName()
                    Option c = new Option(name,name,null,pd.getDescription(), null,null,true,parameter.order());
                    parameterLikes.add(c);
                    // parameterLikes.add(name);
                }
            }
            likes.addAll(parameterLikes);
        }
        return likes;
    }

    @Nonnull
    private static List<Option> _completionOptions(CompletionProcess process) {
        JCommander commander = process.getCommander();
        List<Object> objects = commander.getObjects();
        if (!objects.isEmpty()) {
            Object obj = objects.get(0);
            if (obj instanceof Command) {
                Command command = (Command) obj;
                return command.complete(process);
            }
        }

        return completionOptions(process);
    }

    private static List<Option> completionCommand(CompletionProcess process, String prefix) {
        JCommander commander = process.getCommander();
        List<String> commands = new ArrayList<>();
        for (Map.Entry<String, JCommander> entry : commander.getCommands().entrySet()) {
            commands.add(entry.getKey());
        }
        List<Option> likes = new ArrayList<>();
        String eq = null;
        int order=0;
        for (String s : commands) {
            if (s.equals(prefix)) {
                likes.clear();
                eq = s;
            } else if (s.startsWith(prefix)) {
                Option option =new Option(s,s,null,   commander.getUsageFormatter().getCommandDescription(s), null,null,true,order++);
                likes.add(option);
                //likes.add(s);
            }
        }
        if (eq == null) {
            return likes;
        } else if (process.isEndSpace()) {
            process = cutCommandName(process, eq);
            if (process != null) {
                return _completionOptions(process);
            }
        }
        return likes;
    }

}
