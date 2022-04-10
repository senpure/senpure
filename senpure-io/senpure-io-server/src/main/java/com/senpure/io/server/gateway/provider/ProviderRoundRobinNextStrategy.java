package com.senpure.io.server.gateway.provider;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询可用的服务实例
 *
 * @author senpure
 * @time 2020-06-05 15:07:34
 */
public class ProviderRoundRobinNextStrategy implements ProviderNextStrategy {

    private final AtomicInteger atomicIndex = new AtomicInteger(-1);

    @Override
    public String strategyName() {
        return "round robin";
    }

    @Override
    public Provider next(List<Provider> providers) {
        int index = atomicIndex.incrementAndGet();
        return providers.get(Math.abs(index % providers.size()));
    }
}
