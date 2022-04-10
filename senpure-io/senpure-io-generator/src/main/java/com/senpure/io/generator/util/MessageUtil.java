package com.senpure.io.generator.util;


import com.senpure.base.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class MessageUtil {
    static Logger logger = LoggerFactory.getLogger(MessageUtil.class);


    private static class Mark {

        int index;
        boolean lower;

        @Override
        public String toString() {
            return "Mark{" +
                    "index=" + index +
                    ", lower=" + lower +
                    '}';
        }
    }

    public static String luaNameStyle(String name) {
        name = name.replaceAll("_", "");
        int len = name.length();
        List<Mark> marks = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if (StringUtil.isUpperLetter(c)) {
                int cons = cons(name, i, len);
                //大写结尾
                if (i + cons == len) {
                    Mark mark = new Mark();
                    mark.index = i;
                    mark.lower = false;
                    marks.add(mark);
                    break;
                } else if (cons == 1) {
                    Mark mark = new Mark();
                    mark.index = i;
                    mark.lower = true;
                    if (i > 0) {
                        marks.add(mark);
                    }

                } else {
                    Mark mark = new Mark();
                    mark.index = i;
                    mark.lower = false;
                    if (i > 0) {
                        marks.add(mark);
                    }
                    i += cons - 2;
                }
            }
        }
        int ml = marks.size();
        for (int i = 0; i < ml; i++) {
            Mark mark = marks.get(i);
            if (mark.lower) {
                name = StringUtil.toLowerLetter(name, mark.index);
            }

        }
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        for (int i = 0; i < ml; i++) {
            Mark mark = marks.get(i);
            sb.insert(mark.index + i, "_");
        }
        if (!name.startsWith("_")) {
            sb.insert(0, "_");
        }

        return sb.toString();
    }

    private static int cons(String name, int start, int len) {
        int count = 0;
        for (int i = start; i < len; i++) {
            char c = name.charAt(i);
            if (!StringUtil.isUpperLetter(c)) {
                break;
            }
            count++;
        }
        return count;
    }

    public static void main(String[] args) {
        List<String> names = new ArrayList<>();

        names.add("readBook");
        names.add("computerIP");
        names.add("_book");
        names.add("groupIPPConfig");
        names.add("CSStudentMessage");
        for (String name : names) {

            System.out.println(name + " >> " + luaNameStyle(name));
        }

    }
}
