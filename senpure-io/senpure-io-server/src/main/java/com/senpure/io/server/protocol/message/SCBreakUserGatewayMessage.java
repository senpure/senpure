package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

/**
 * 断开用户与网关
 * 
 * @author senpure
 * @time 2020-11-20 17:37:42
 */
public class SCBreakUserGatewayMessage extends CompressMessage {

    public static final int MESSAGE_ID = 110;

    public void copy(SCBreakUserGatewayMessage source) {
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
    }

    /**
     * 读取字节缓存
     */
    @Override
    public void read(ByteBuf buf, int maxIndex) {
        while (true) {
            int tag = readTag(buf, maxIndex);
            switch (tag) {
                case 0://end
                    return;
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
        serializedSize = size ;
        return size ;
    }


    @Override
    public int messageId() {
        return 110;
    }

    @Override
    public String toString() {
        return "SCBreakUserGatewayMessage[110]{"
                + "}";
    }

    @Override
    public String toString(String indent) {
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCBreakUserGatewayMessage").append("[110]").append("{");
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}