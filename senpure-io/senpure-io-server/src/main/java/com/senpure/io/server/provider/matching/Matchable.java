package com.senpure.io.server.provider.matching;

import com.senpure.io.server.protocol.bean.Consumer;

import java.util.List;

public interface Matchable {
    long matchableId();

    void match(List<Consumer> consumers,int timeout);
}
