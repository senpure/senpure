package com.senpure.io.server.support;


import com.senpure.io.server.protocol.bean.IdName;
import com.senpure.io.server.protocol.message.CSHeartMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MessageIdReader
 *
 * @author senpure
 * @time 2019-02-15 16:48:04
 */
public class MessageIdReader {

    private static Map<Integer, String> idMaps = new HashMap<>();

    public static String read(int messageId) {
        return idMaps.computeIfAbsent(messageId, aLong -> aLong + "");
    }

    public static void relation(Integer messageId, String value) {
        idMaps.put(messageId, messageId + " [" + value + "]");
    }

    public static void relation(List<IdName> idNames) {
        for (IdName idName : idNames) {
            relation(idName.getId(), idName.getMessageName());
        }
    }

    static {
        relation(MessageScanner.scan("com.senpure.io.server.protocol.message"));
    }

    public static void main(String[] args) {
        System.out.println(MessageIdReader.read(CSHeartMessage.MESSAGE_ID));
        System.out.println(MessageIdReader.read(1105));
        System.out.println(MessageIdReader.read(1104));
    }
}
