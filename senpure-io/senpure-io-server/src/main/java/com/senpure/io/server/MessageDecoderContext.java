package com.senpure.io.server;


public interface  MessageDecoderContext {

    void registerDecoder(MessageDecoder<?> decoder);

    MessageDecoder<?>  decoder(int messageId);


}
