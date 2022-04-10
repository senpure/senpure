package com.senpure.io.server.protocol.message;

import com.senpure.io.server.protocol.bean.Statistic;
import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * 向网关上报统计信息
 * 
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class CSStatisticMessage extends CompressMessage {

    public static final int MESSAGE_ID = 119;
    private Statistic statistic;

    public void copy(CSStatisticMessage source) {
        if (source.getStatistic() != null) {
            Statistic tempStatistic = new Statistic();
            tempStatistic.copy(source.getStatistic());
            this.statistic = tempStatistic;
        } else {
            this.statistic = null;
            }
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        if (statistic != null) {
            writeBean(buf, 11, statistic);
        }
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
                case 11:// 1 << 3 | 3
                    statistic = new Statistic();
                    readBean(buf,statistic);
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
        if (statistic != null) {
             //tag size 11
            size += computeBeanSize(1, statistic);
        }
        serializedSize = size ;
        return size ;
    }

    public Statistic getStatistic() {
        return statistic;
    }

    public CSStatisticMessage setStatistic(Statistic statistic) {
        this.statistic = statistic;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_CS;
    }

    @Override
    public int messageId() {
        return 119;
    }

    @Override
    public String toString() {
        return "CSStatisticMessage[119]{"
                + "statistic=" + statistic
                + "}";
    }

    @Override
    public String toString(String indent) {
        //9 + 3 = 12 个空格
        String nextIndent = "            ";
        //最长字段长度 9
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSStatisticMessage").append("[119]").append("{");
        sb.append("\n");
        sb.append(indent).append("statistic = ");
        if (statistic != null){
            sb.append(statistic.toString(indent+nextIndent));
        } else {
            sb.append("null");
        }
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}