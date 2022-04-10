package com.senpure.io.server.consumer;

import com.senpure.io.protocol.Message;
import com.senpure.io.server.remoting.AbstractSameServerSingleInstanceMessageSender;

import java.util.HashSet;
import java.util.Set;

public class ProviderManager extends AbstractSameServerSingleInstanceMessageSender<ConsumerMessage,Provider> {

    @Override
    public ConsumerMessage createMessage(Message message) {

        return new ConsumerMessage(message);
    }

    @Override
    public ConsumerMessage createMessage(Message message, boolean requestId) {
        ConsumerMessage frame = new ConsumerMessage(message);
        if (requestId) {
            frame.setRequestId(nextRequestId());
        }
        return frame;
    }

    @Override
    public ConsumerMessage createMessage(Message message, int requestId) {
        ConsumerMessage frame = new ConsumerMessage(message);
        frame.setRequestId(requestId);
        return frame;
    }

    public static void main(String[] args) {
        ProviderManager providerManager = new ProviderManager();
        providerManager.atomicRequestId.set(Integer.MAX_VALUE -20);

        Set<Integer> integers = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Thread thread=new Thread(() -> {
                for (int j = 0; j < 8; j++) {
                    int id = providerManager.nextRequestId();
                    integers.add(id);
                    System.out.println(Thread.currentThread().getName()+" "+id);
//                    if (!integers.add(id)) {
//
//                        Assert.error("" + id);
//                    }
                }
            });
            thread.start();

        }


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(integers.size());
    }


}
