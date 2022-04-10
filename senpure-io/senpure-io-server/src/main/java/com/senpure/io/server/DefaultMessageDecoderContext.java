package com.senpure.io.server;

import com.senpure.base.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class DefaultMessageDecoderContext implements MessageDecoderContext {

    protected final  Map<Integer, MessageDecoder<?>> decoderMap = new HashMap<>();

    @Override
    public void registerDecoder(MessageDecoder<?> decoder) {
        MessageDecoder<?> last = decoderMap.get(decoder.messageId());
        if (last != null) {
            Assert.error( decoder.messageId() + " 解码程序已经存在 -> 新注册" +
                    "[" + decoder.getClass().getName() + "]" +
                    " -> 已注册" + "[" + last.getClass().getName() + "]");
        }
        decoderMap.put(decoder.messageId(), decoder);
    }

    @Override
    public MessageDecoder<?> decoder(int messageId) {
        return decoderMap.get(messageId);
    }

}
