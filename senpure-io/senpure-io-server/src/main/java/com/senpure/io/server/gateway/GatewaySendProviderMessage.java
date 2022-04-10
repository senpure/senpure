package com.senpure.io.server.gateway;

import com.senpure.io.server.MessageFrame;

public interface GatewaySendProviderMessage extends MessageFrame {

    long token();

    long userId();

}
