package com.senpure.base.util;


import com.senpure.base.AppEvn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BannerShow {
    private static Logger LOGGER = LoggerFactory.getLogger(BannerShow.class);

    static List<String> strs = new ArrayList<>(16);

    static {
        strs.add("      ___           ___           ___           ___           ___           ___           ___");
        strs.add("     /  /\\         /__/\\         /  /\\         /  /\\         /  /\\         /  /\\         /  /\\");
        strs.add("    /  /:/_        \\  \\:\\       /  /:/        /  /:/        /  /:/_       /  /:/_       /  /:/_");
        strs.add("   /  /:/ /\\        \\  \\:\\     /  /:/        /  /:/        /  /:/ /\\     /  /:/ /\\     /  /:/ /\\");
        strs.add("  /  /:/ /::\\   ___  \\  \\:\\   /  /:/  ___   /  /:/  ___   /  /:/ /:/_   /  /:/ /::\\   /  /:/ /::\\");
        strs.add(" /__/:/ /:/\\:\\ /__/\\  \\__\\:\\ /__/:/  /  /\\ /__/:/  /  /\\ /__/:/ /:/ /\\ /__/:/ /:/\\:\\ /__/:/ /:/\\:\\");
        strs.add(" \\  \\:\\/:/~/:/ \\  \\:\\ /  /:/ \\  \\:\\ /  /:/ \\  \\:\\ /  /:/ \\  \\:\\/:/ /:/ \\  \\:\\/:/~/:/ \\  \\:\\/:/~/:/");
        strs.add("  \\  \\::/ /:/   \\  \\:\\  /:/   \\  \\:\\  /:/   \\  \\:\\  /:/   \\  \\::/ /:/   \\  \\::/ /:/   \\  \\::/ /:/");
        strs.add("   \\__\\/ /:/     \\  \\:\\/:/     \\  \\:\\/:/     \\  \\:\\/:/     \\  \\:\\/:/     \\__\\/ /:/     \\__\\/ /:/");
        strs.add("     /__/:/       \\  \\::/       \\  \\::/       \\  \\::/       \\  \\::/        /__/:/        /__/:/");
        strs.add("     \\__\\/         \\__\\/         \\__\\/         \\__\\/         \\__\\/         \\__\\/         \\__\\/");
    }

    //控制台对于不同的字体宽字符有不同的表现，边框修饰默认使用单字符
    private static String topLeft = "*";
    private static String topRight = "*";
    private static String downLeft = "*";
    private static String downRight = "*";
    private static String topDown = "*";
    private static String leftRight = "*";

    private static boolean topDownHalf;

    public static void show() {
        //LOGGER.debug("\n\n{}", str());
        System.out.println(str());
    }


    public static String str() {
        List<String> strs = null;
        try {
            InputStream
                    inputStream = BannerShow.class.getClassLoader().getResourceAsStream("show-banner.txt");
            // 建立一个输入流对象reader
            if (inputStream != null) {
                InputStreamReader reader = new InputStreamReader(
                        inputStream);
                BufferedReader br = new BufferedReader(reader);
                strs = new ArrayList<>();
                while (br.ready()) {
                    String line = br.readLine();
                    strs.add(line);
                }
                br.close();
                reader.close();
                inputStream.close();
            }

        } catch (IOException ignored) {

        }
        if (strs == null || strs.size() == 0) {
            strs = BannerShow.strs;
        }
        int length = 0;
        for (int i = 0; i < strs.size(); i++) {
            int tl = strs.get(i).length();
            length = Math.max(tl, length);
        }
        if (length % 2 != 0) {
            length++;
        }

        StringBuilder banner = new StringBuilder();
        String top = getStr(length);
        // top = "╔" + top + "╗";
        //top = "*" + top + "*";
        top = topLeft + top + topRight;
        top = AnsiOutput.toString(AnsiColor.BRIGHT_RED, top);
        banner.append(top);
        banner.append("\n");

        for (int i = 0; i < strs.size(); i++) {
            // banner.append(AnsiOutput.toString(AnsiColor.BRIGHT_RED, "║"));
            // banner.append(AnsiOutput.toString(AnsiColor.BRIGHT_RED, "*"));
            banner.append(AnsiOutput.toString(AnsiColor.BRIGHT_RED, leftRight));
            banner.append(AnsiOutput.toString(AnsiColor.BRIGHT_CYAN, strs.get(i)));
            int l = strs.get(i).length();
            String temp = getStr(" ", length - l);
            banner.append(AnsiOutput.toString(AnsiColor.BRIGHT_RED, temp + leftRight));
            //  banner.append(AnsiOutput.toString(AnsiColor.BRIGHT_RED, temp + "*"));
            banner.append("\n");
        }
        //  String empty;
        // empty = getStr(" ", length);
        //  empty = "║" + empty + "║";
        // empty = "*" + empty + "*";
        // empty = AnsiOutput.toString(AnsiColor.BRIGHT_RED, empty);
        // banner.append(empty);
        // banner.append("\n");
        String down = getStr( length);

        // down = "╚" + down + "╝";
        //down = "*" + down + "*";
        down = downLeft + down + downRight;
        down = AnsiOutput.toString(AnsiColor.BRIGHT_RED, down);
        banner.append(down);
        banner.append("\n");
        return banner.toString();
    }

    private static String getStr(int num) {
        //LOGGER.info("ch {} num {}", ch, num);

        if (topDownHalf) {
            return getStr(topDown, num >> 1);
        } else {
            return getStr(topDown, num);
        }
    }

    private static String getStr(String ch, int num) {
        //LOGGER.info("ch {} num {}", ch, num);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append(ch);

        }
        return sb.toString();
    }

    public static void bannerText(String name) {


        try {
            InputStream inputStream;
            inputStream = BannerShow.class.getClassLoader().getResourceAsStream(name);
            // 建立一个输入流对象reader
            InputStreamReader reader = new InputStreamReader(
                    inputStream);
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();

                line = line.replace("\\", "\\\\");
                System.out.println("strs.add(\"" + line + "\") ;");
            }
            br.close();
            reader.close();
            inputStream.close();
        } catch (IOException ignored) {

        }
    }


    public static String getTopLeft() {
        return topLeft;
    }

    public static void setTopLeft(String topLeft) {
        BannerShow.topLeft = topLeft;
    }

    public static String getTopRight() {
        return topRight;
    }

    public static void setTopRight(String topRight) {
        BannerShow.topRight = topRight;
    }

    public static String getDownLeft() {
        return downLeft;
    }

    public static void setDownLeft(String downLeft) {
        BannerShow.downLeft = downLeft;
    }

    public static String getDownRight() {
        return downRight;
    }

    public static void setDownRight(String downRight) {
        BannerShow.downRight = downRight;
    }

    public static String getTopDown() {
        return topDown;
    }

    public static void setTopDown(String topDown) {
        BannerShow.topDown = topDown;
    }

    public static String getLeftRight() {
        return leftRight;
    }

    public static void setLeftRight(String leftRight) {
        BannerShow.leftRight = leftRight;
    }

    public static boolean isTopDownHalf() {
        return topDownHalf;
    }

    public static void setTopDownHalf(boolean topDownHalf) {
        BannerShow.topDownHalf = topDownHalf;
    }

    public static void main(String[] args) {
        System.out.println(Charset.defaultCharset());
        System.setProperty("PID", Objects.requireNonNull(AppEvn.getPid()));
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);

        bannerText("show-banner.txt");

        show();
    }
}
