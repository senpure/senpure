package com.senpure.cli;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnsiConsoleCommandProcess implements CommandProcess {
    private static final String ENCODE_START = "\u001b[";
    private static final String ENCODE_END = "m";
    private static final String ENCODE_JOIN = ";";
    private static final String RESET = "0;39";
    private boolean enable;

    public AnsiConsoleCommandProcess() {
    }

    @Nonnull
    private AnsiColor color(Color color) {

        return COLOR_MAP.get(color);
    }


    @Override
    public void feed(String feed) {
        if (enable) {

            feed = completion(feed);
        }
        System.out.println(feed);
    }

    public AnsiConsoleCommandProcess(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String color(Color color, String element) {
        return encode(color, element);
    }


    private String encode(Color color, String element) {
        if (enable) {
            element = ENCODE_START + color(color).code + ENCODE_END + element;
            element += ENCODE_START + RESET + ENCODE_END;
            return element;
        }

        return element;
    }

    private static final Map<Color, AnsiColor> COLOR_MAP = new HashMap<>();

    static {
        for (Color value : Color.values()) {

            AnsiColor ansiColor = null;

            for (AnsiColor color : AnsiColor.values()) {
                if (value.name().equals(color.name())) {
                    ansiColor = color;
                    break;
                }
            }
            ansiColor = ansiColor == null ? AnsiColor.DEFAULT : ansiColor;
            COLOR_MAP.put(value, ansiColor);

        }

    }

    /**
     * 补全ansi代码
     * 默认情况下不支持镶套的ansi代码，该方法将补充一镶套的代码使其支持镶套
     * <br>
     * example [33mYELLOW[[35mMAGENTA[[36mCYAN[0;39m][0;39m[32mGREEN[[31mRED][0;39m][0;39m][0;39m
     * <br>↓↓↓↓↓↓ <br>
     * result [33mYELLOW[[35mMAGENTA[[36mCYAN[0;39m[35m][0;39m[33m[32mGREEN[[31mRED][0;39m[32m][0;39m[33m][0;39m
     *
     * @param str
     * @return
     */
    private static String completion(String str) {
        String regex = "(\\u001b\\[\\d{2}m)|(\\u001b\\[0;39m)";
        Pattern startPattern = Pattern.compile(regex);
        Matcher startMatcher = startPattern.matcher(str);
        Tree tree = new Tree();
        while (startMatcher.find()) {
            Node node = new Node();
            node.end = startMatcher.end();
            node.code = startMatcher.group();
            node.left = !node.code.equals("\u001b[0;39m");
            //  node.code = "\u001B[0;39m"+node.code;
            tree.addNode(node);

        }

        if (tree.nodes.isEmpty()) {
            return str;
        }
        int raiseIndex = 0;
        StringBuilder sb = new StringBuilder(str);
        for (Node node : tree.nodes) {
            raiseIndex = traversal(node, sb, raiseIndex);
        }

        return sb.toString();
    }

    private static int traversal(Node node, StringBuilder sb, int raiseIndex) {

        int codeLength = node.code.length();
        for (Node child : node.children) {
            raiseIndex = traversal(child, sb, raiseIndex);
            if (child.right != null) {
                int offset = child.right.end + raiseIndex;
                // System.out.println("offset:"+offset+"  "+sb.substring(0,offset));
                sb.insert(offset, node.code);
                raiseIndex += codeLength;
            }

        }
        return raiseIndex;
    }

    private enum AnsiColor {
        DEFAULT("39"),
        BLACK("30"),
        RED("31"),
        GREEN("32"),
        YELLOW("33"),
        BLUE("34"),
        MAGENTA("35"),
        CYAN("36"),
        WHITE("37"),
        BRIGHT_BLACK("90"),
        BRIGHT_RED("91"),
        BRIGHT_GREEN("92"),
        BRIGHT_YELLOW("93"),
        BRIGHT_BLUE("94"),
        BRIGHT_MAGENTA("95"),
        BRIGHT_CYAN("96"),
        BRIGHT_WHITE("97");

        private final String code;

        private AnsiColor(String code) {
            this.code = code;
        }

        public String toString() {
            return this.code;
        }
    }

    private static class Node {

        private final List<Node> children = new ArrayList<>();

        private Node parent;
        private int end;
        private String code;
        private boolean left;
        private Node right;


    }

    private static class Tree {

        private final List<Node> nodes = new ArrayList<>();
        private Node current;

        public void addNode(Node node) {
            if (node == null) {
                return;
            }
            if (current == null) {

                current = node;

                nodes.add(node);
            } else {

                if (node.left) {
                    current.children.add(node);
                    node.parent = current;
                    current = node;
                } else {
                    current.right = node;
                    node.parent = current.parent;
                    current = current.parent;
                }
            }


        }


    }
}
