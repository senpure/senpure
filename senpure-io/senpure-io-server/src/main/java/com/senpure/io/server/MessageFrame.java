package com.senpure.io.server;


import com.senpure.io.protocol.Message;

public interface MessageFrame {
    int MESSAGE_TYPE_CS = 1;
    int MESSAGE_TYPE_SC = 2;

    int messageType();

    int requestId();


    int messageId();

    Message message();

}
