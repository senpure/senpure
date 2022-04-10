package com.senpure.io.server.provider.matching;

import com.senpure.io.server.protocol.bean.Consumer;

import java.util.List;

public class AbstractMatchable  implements  Matchable{
    @Override
    public long matchableId() {
        return 0;
    }

    @Override
    public void match(List<Consumer> consumers, int timeout) {

    }
}
