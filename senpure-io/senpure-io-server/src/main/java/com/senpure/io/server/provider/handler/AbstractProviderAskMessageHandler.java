package com.senpure.io.server.provider.handler;


import com.senpure.io.protocol.Message;


public abstract class AbstractProviderAskMessageHandler<T extends Message> extends AbstractProviderMessageHandler<T> implements ProviderAskMessageHandler<T> {


    @Override
    public boolean direct() {
        return false;
    }



}
