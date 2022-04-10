package com.senpure.io.protocol;

import io.netty.buffer.ByteBuf;

/**
 * Bean 约定构建完成后就不再更改
 *
 * @author senpure
 * @time 2020-01-06 10:45:39
 */
public interface Bean {
    /**
     * 写入数据
     *
     * @param buf buf
     */
    void write(ByteBuf buf);

    /**
     * 读取数据
     *
     * @param buf buf
     * @param maxIndex 读到该位置则不读取了
     */
    void read(ByteBuf buf, int maxIndex);

    /**
     * @param indent 缩进
     * @return
     */
    String toString(String indent);

    /**
     * 序列化后的大小
     *
     * @return
     */
    int serializedSize();
}
