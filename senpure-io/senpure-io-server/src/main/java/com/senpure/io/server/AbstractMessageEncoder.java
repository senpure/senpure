package com.senpure.io.server;

import com.senpure.io.protocol.CompressBean;
import com.senpure.io.protocol.Message;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractMessageEncoder<I> extends MessageToByteEncoder<I> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    //包长int ,消息Id int, 二进制数据
    public void encode(ByteBuf out, int requestId, Message message) {
        int headLength = CompressBean.computeVar32Size(message.messageType());
        headLength += CompressBean.computeVar32Size(requestId);
        headLength += CompressBean.computeVar32Size(message.messageId());
        int packageLength = headLength + message.serializedSize();
        out.ensureWritable(CompressBean.computeVar32Size(packageLength) + packageLength);
        CompressBean.writeVar32(out, packageLength);
        CompressBean.writeVar32(out, message.messageType());
        CompressBean.writeVar32(out, requestId);
        CompressBean.writeVar32(out, message.messageId());
        message.write(out);
    }


}
