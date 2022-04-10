package com.senpure.io.server.protocol.message;

import com.senpure.io.server.protocol.bean.Consumer;
import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.ArrayList;

/**
 * @author senpure
 * @time 2021-5-31 22:16:30
 */
public class CSConsumerDispatchMessage extends CompressMessage {

    public static final int MESSAGE_ID = 125;
    private List<Consumer> consumers = new ArrayList<>(16);
    //超时
    private int timeout;

    public void copy(CSConsumerDispatchMessage source) {
        this.consumers.clear();
        for (Consumer consumer : source.getConsumers()) {
            Consumer tempConsumer = new Consumer();
            tempConsumer.copy(consumer);
        }
        this.timeout = source.getTimeout();
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        for (Consumer value : consumers) {
             writeBean(buf, 11, value);
        }
        //超时
        writeVar32(buf, 16, timeout);
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
                    Consumer tempConsumersBean = new Consumer();
                    readBean(buf,tempConsumersBean);
                    consumers.add(tempConsumersBean);
                    break;
                //超时
                case 16:// 2 << 3 | 0
                    timeout = readVar32(buf);
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
        for (Consumer value : consumers) {
            size += computeBeanSize(1, value);
        }
        //超时
        //tag size 16
        size += computeVar32Size(1, timeout);
        serializedSize = size ;
        return size ;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public CSConsumerDispatchMessage setConsumers(List<Consumer> consumers) {
        if (consumers == null) {
            this.consumers = new ArrayList<>(16);
            return this;
        }
        this.consumers = consumers;
        return this;
    }

    /**
     * get 超时
     *
     * @return
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * set 超时
     */
    public CSConsumerDispatchMessage setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_CS;
    }

    @Override
    public int messageId() {
        return 125;
    }

    @Override
    public String toString() {
        return "CSConsumerDispatchMessage[125]{"
                + "consumers=" + consumers
                + ",timeout=" + timeout
                + "}";
    }

    @Override
    public String toString(String indent) {
        //9 + 3 = 12 个空格
        String nextIndent = "            ";
        //最长字段长度 9
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSConsumerDispatchMessage").append("[125]").append("{");
        sb.append("\n");
        sb.append(indent).append("consumers = ");
        appendBeans(sb,consumers,indent,nextIndent);
        //超时
        sb.append("\n");
        sb.append(indent).append("timeout   = ").append(timeout);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}