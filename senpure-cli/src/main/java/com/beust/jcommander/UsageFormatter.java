package com.beust.jcommander;

import com.beust.jcommander.internal.Lists;
import com.senpure.cli.Color;
import com.senpure.cli.Command;
import com.senpure.cli.CommandProcess;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * UsageFormatter
 *
 * @author senpure
 * @time 2020-07-23 16:07:04
 */
public class UsageFormatter extends DefaultUsageFormatter {


    private final JCommander commander;

    public UsageFormatter(JCommander commander) {
        super(commander);
        this.commander = commander;
    }


    public void usage(StringBuilder out, String indent) {
        if (commander.getDescriptions() == null) {
            commander.createDescriptions();
        }
        boolean hasCommands = !commander.getCommands().isEmpty();
        boolean hasOptions = !commander.getDescriptions().isEmpty();

        // Indentation constants
        final int descriptionIndent = 6;
        final int indentCount = indent.length() + descriptionIndent;

        // Append first line (aka main line) of the usage
        appendMainLine(out, hasOptions, hasCommands, indentCount, indent);

        // Align the descriptions at the "longestName" column
        int longestName = 0;
        List<ParameterDescription> sortedParameters = Lists.newArrayList();

        for (ParameterDescription pd : commander.getFields().values()) {
            if (!pd.getParameter().hidden()) {
                sortedParameters.add(pd);
                // + to have an extra space between the name and the description
                int length = pd.getNames().length() + 2;

                if (length > longestName) {
                    longestName = length;
                }
            }
        }

        // Sort the options
        sortedParameters.sort((x, y) -> {
            WrappedParameter a = x.getParameter();
            WrappedParameter b = y.getParameter();
            if (a != null && b != null) {
                return Integer.compare(a.order(), b.order());
            }
            return x.getLongestName().compareTo(y.getLongestName());
        });

        // Append all the parameter names and descriptions
        appendAllParametersDetails(out, indentCount, indent, sortedParameters);

        // Append commands if they were specified
        if (hasCommands) {
            appendCommands(out, indentCount, descriptionIndent, indent);
        }
    }


    @Override
    public void appendMainLine(StringBuilder out, boolean hasOptions, boolean hasCommands, int indentCount, String indent) {
        String programName = commander.getProgramDisplayName() != null
                ? commander.getProgramDisplayName() : "<main class>";
        StringBuilder mainLine = new StringBuilder();
        mainLine.append(indent)
                //.append("Usage: ")
                .append(programName);

        if (hasOptions) {
            mainLine.append(" [options]");
        }

        if (hasCommands) {
            mainLine.append(indent).append(" [command] [command options]");
        }
        JCommander.MainParameter mainParameter = commander.getMainParameter();

        if (mainParameter != null) {

            if (mainParameter.getDescription().getParameter().required()) {
                mainLine.append(" ").append(mainParameter.getDescription().getParameterized().getName());
            } else {
                mainLine.append(" <").append(mainParameter.getDescription().getParameterized().getName()).append(">");
            }
            // mainLine.append(" ").append(commander.getMainParameter().getDescription().getDescription());
        }
        wrapDescription(out, indentCount, mainLine.toString());
        out.append("\n");
    }

    @Override
    public void appendCommands(StringBuilder out, int indentCount, int descriptionIndent, String indent) {

        appendCommands(out, indent, true);
    }

    public void appendCommands(StringBuilder out, String indent, boolean recursion) {
        out.append(indent).append("  Commands:\n");

        List<JCommander.ProgramName> programNames = new ArrayList<>();
        // The magic value 3 is the number of spaces between the name of the option and its description
        for (Map.Entry<JCommander.ProgramName, JCommander> commands : commander.getRawCommands().entrySet()) {
            Object arg = commands.getValue().getObjects().get(0);
            Parameters p = arg.getClass().getAnnotation(Parameters.class);
            if (p == null || !p.hidden()) {
                programNames.add(commands.getKey());
            }
        }
        int maxLen = 0;
        for (JCommander.ProgramName name : programNames) {
            maxLen = Math.max(maxLen, name.getDisplayName().length());
        }
        for (JCommander.ProgramName programName : programNames) {
            String displayName = programName.getDisplayName();
            int len = maxLen - displayName.length();
            String description = indent + s(4) + displayName + s(4 + len) + getCommandDescription(programName.getName());

            int lineIndent = indent.length() + 4 + maxLen + 4;
            wrapDescription(out, lineIndent, description);
            out.append("\n");

            if (recursion) {
                JCommander jc = commander.findCommandByAlias(programName.getName());
                checkUsageFormatter(jc);

                //4+2
                jc.getUsageFormatter().usage(out, indent + s(6));
                out.append("\n");
            }
        }
    }

    public static void checkUsageFormatter(JCommander jCommander) {
        if (!(jCommander.getUsageFormatter() instanceof UsageFormatter)) {
            UsageFormatter usageFormatter = new UsageFormatter(jCommander);
            jCommander.setUsageFormatter(usageFormatter);
        }
    }


    @Override
    public void appendAllParametersDetails(StringBuilder out, int indentCount, String indent,
                                           List<ParameterDescription> sortedParameters) {
        if (sortedParameters.size() > 0) {
            out.append(indent).append("  Options:\n");
        }

        for (ParameterDescription pd : sortedParameters) {
            WrappedParameter parameter = pd.getParameter();
            String description = pd.getDescription();
            boolean hasDescription = !description.isEmpty();

            CommandProcess commandProcess = Command.COMMAND_PROCESS_THREAD_LOCAL.get();
            // First line, command name
            out.append(indent)
                    .append("  ")
                    .append(parameter.required() ? commandProcess.color(Color.RED,"* ") : "  ")
                    .append(pd.getNames())
                    .append("\n");

            if (hasDescription) {
                wrapDescription(out, indentCount, s(indentCount) + description);
            }
            Object def = pd.getDefault();

            if (pd.isDynamicParameter()) {
                String syntax = "Syntax: " + parameter.names()[0] + "key" + parameter.getAssignment() + "value";

                if (hasDescription) {
                    out.append(newLineAndIndent(indentCount));
                } else {
                    out.append(s(indentCount));
                }
                out.append(syntax);
            }

            if (def != null && !pd.isHelp()) {
                String displayedDef = Strings.isStringEmpty(def.toString()) ? "<empty string>" : def.toString();
               // String defaultText = "Default: " + commandProcess.color (Color.BRIGHT_BLUE,(parameter.password() ? "********" : displayedDef));
                String defaultText = "Default: " + (parameter.password() ? "********" : displayedDef);
                if (hasDescription) {
                    out.append(newLineAndIndent(indentCount));
                } else {
                    out.append(s(indentCount));
                }
                out.append(defaultText);
            }
            Class<?> type = pd.getParameterized().getType();

            if (type.isEnum()) {
                String valueList = EnumSet.allOf((Class<? extends Enum>) type).toString();
                String possibleValues = "Possible Values: " + valueList;

                // Prevent duplicate values list, since it is set as 'Options: [values]' if the description
                // of an enum field is empty in ParameterDescription#init(..)
                if (!description.contains("Options: " + valueList)) {
                    if (hasDescription) {
                        out.append(newLineAndIndent(indentCount));
                    } else {
                        out.append(s(indentCount));
                    }
                    out.append(possibleValues);
                }
            }
            out.append("\n");
        }
    }

    private static String newLineAndIndent(int indent) {
        return "\n" + s(indent);
    }

    @Override
    public void wrapDescription(StringBuilder out, int indent, int currentLineIndent, String description) {
        int max = commander.getColumnSize();
        if ((max & 1) == 1) {
            max--;
        }
        String[] words = description.split("\n");
        // System.out.println(Arrays.toString(words));
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0 && currentLineIndent > 0) {
                word = s(currentLineIndent) + word;
            }
            if (word.length() > max) {
                appendMultiLine(out, word, max, indent, true);

            } else {
                if (i == 0) {
                    out.append(word);
                } else {
                    out.append(s(indent)).append(word);
                }
                if (i < words.length - 1) {
                    out.append("\n");
                }
            }


        }
    }

    private void appendMultiLine(StringBuilder out, String word, int max, int indent, boolean first) {

        // int len = word.length() + (first ? 0 : indent -2);

        int newLineIndent = 2;
        int lineIndex = first ? max : max - indent - newLineIndent;
        int len = word.length();
        int realIndex = 0;
        int lineLen = 0;
        boolean cut = false;
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);
            if (isChinese(c)) {
                lineLen += 2;

            } else {
                lineLen++;
            }
            if (lineLen > lineIndex) {
                cut = true;
                break;
            }
            realIndex++;
        }
        if (cut) {
            String s = word.substring(0, realIndex);
            //System.err.println("index " + realIndex + " " + s);
            if (first) {
                out.append(s);
            } else {
                out.append(s(indent + newLineIndent)).append(s);
            }
            out.append("\n");
            String left = word.substring(realIndex);
            appendMultiLine(out, left, max, indent, false);
        } else {
            if (first) {
                out.append(word);
            } else {
                out.append(s(indent + newLineIndent)).append(word);
            }
        }
    }

    public static String s(int count) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            result.append(" ");
        }
        return result.toString();
    }

    private static boolean isChinese(char c) {
        if (c >= 0x4E00 && c <= 0x9FA5) {
            return true;
        }
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static void main(String[] args) {

        String str = "AAAAA\nBBBBB\nCCCCC";

        UsageFormatter usageFormatter = new UsageFormatter(new JCommander());

        StringBuilder out = new StringBuilder();
        usageFormatter.wrapDescription(out, 0, 0, str);

        System.out.println(out.toString());

    }
}
