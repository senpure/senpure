package com.senpure.io.protocol;

/**
 * Message
 *
 * @author senpure
 * @time 2020-01-06 10:51:26
 */
public interface Message extends Bean {

    int MESSAGE_TYPE_CS = 1;
    int MESSAGE_TYPE_SC = 2;

    /**
     * 消息唯一标识
     */
    int messageId();

    int messageType();

}
