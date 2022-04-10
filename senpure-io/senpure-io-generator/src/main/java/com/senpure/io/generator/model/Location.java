package com.senpure.io.generator.model;

/**
 * Location
 *
 * @author senpure
 * @time 2019-05-16 14:13:36
 */
public class Location {
    //出错时使用所在io协议文件的行数最低为1
    private int line;
    //出错时使用所在io协议文件的位置最低为0
    private int position;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "line " + line + ":" + position+" ";
    }
}
