package com.senpure.io.server.protocol.bean;

import com.senpure.io.protocol.CompressBean;
import io.netty.buffer.ByteBuf;

/**
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class Statistic extends CompressBean {
    //分数0-100
    private int score;

    public void copy(Statistic source) {
        this.score = source.getScore();
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        //分数0-100
        writeVar32(buf, 8, score);
    }

    /**
     * 读取字节缓存
     */
    @Override
    public void read(ByteBuf buf, int endIndex) {
        while (true) {
            int tag = readTag(buf, endIndex);
            switch (tag) {
                case 0://end
                    return;
                //分数0-100
                case 8:// 1 << 3 | 0
                    score = readVar32(buf);
                    break;
                default://skip
                    skip(buf, tag);
                    break;
            }
        }
    }

    private int serializedSize = -1;

    @Override
    public int serializedSize() {
        int size = serializedSize;
        if (size != -1) {
            return size;
        }
        size = 0;
        //分数0-100
        //tag size 8
        size += computeVar32Size(1, score);
        serializedSize = size ;
        return size ;
    }

    /**
     * get 分数0-100
     *
     * @return
     */
    public int getScore() {
        return score;
    }

    /**
     * set 分数0-100
     */
    public Statistic setScore(int score) {
        this.score = score;
        return this;
    }

    @Override
    public String toString() {
        return "Statistic{"
                + "score=" + score
                + "}";
    }

    @Override
    public String toString(String indent) {
        //最长字段长度 5
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("Statistic").append("{");
        //分数0-100
        sb.append("\n");
        sb.append(indent).append("score = ").append(score);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}