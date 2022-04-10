package com.senpure.io.server.protocol.message;

import com.senpure.io.server.protocol.bean.Consumer;
import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.ArrayList;

/**
 * 向网关请求将目标分配到一台实例
 * 
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class CSMatchingConsumerMessage extends CompressMessage {

    public static final int MESSAGE_ID = 121;
    private String serverName;
    private List<Consumer> consumers = new ArrayList<>(16);
    //超时 毫秒
    private int timeout;

    public void copy(CSMatchingConsumerMessage source) {
        this.serverName = source.getServerName();
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
        if (serverName != null) {
            writeString(buf, 11, serverName);
        }
        for (Consumer value : consumers) {
             writeBean(buf, 19, value);
        }
        //超时 毫秒
        writeVar32(buf, 24, timeout);
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
                    serverName = readString(buf);
                    break;
                case 19:// 2 << 3 | 3
                    Consumer tempConsumersBean = new Consumer();
                    readBean(buf,tempConsumersBean);
                    consumers.add(tempConsumersBean);
                    break;
                //超时 毫秒
                case 24:// 3 << 3 | 0
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
        if (serverName != null) {
             //tag size 11
             size += computeStringSize(1, serverName);
        }
        for (Consumer value : consumers) {
            size += computeBeanSize(1, value);
        }
        //超时 毫秒
        //tag size 24
        size += computeVar32Size(1, timeout);
        serializedSize = size ;
        return size ;
    }

    public String getServerName() {
        return serverName;
    }

    public CSMatchingConsumerMessage setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public CSMatchingConsumerMessage setConsumers(List<Consumer> consumers) {
        if (consumers == null) {
            this.consumers = new ArrayList<>(16);
            return this;
        }
        this.consumers = consumers;
        return this;
    }

    /**
     * get 超时 毫秒
     *
     * @return
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * set 超时 毫秒
     */
    public CSMatchingConsumerMessage setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_CS;
    }

    @Override
    public int messageId() {
        return 121;
    }

    @Override
    public String toString() {
        return "CSMatchingConsumerMessage[121]{"
                + "serverName=" + serverName
                + ",consumers=" + consumers
                + ",timeout=" + timeout
                + "}";
    }

    @Override
    public String toString(String indent) {
        //10 + 3 = 13 个空格
        String nextIndent = "             ";
        //最长字段长度 10
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("CSMatchingConsumerMessage").append("[121]").append("{");
        sb.append("\n");
        sb.append(indent).append("serverName = ").append(serverName);
        sb.append("\n");
        sb.append(indent).append("consumers  = ");
        appendBeans(sb,consumers,indent,nextIndent);
        //超时 毫秒
        sb.append("\n");
        sb.append(indent).append("timeout    = ").append(timeout);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}