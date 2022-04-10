package com.senpure.io.server.gateway.provider;

import java.util.List;

/**
 * NextProducer
 *
 * @author senpure
 * @time 2020-06-05 15:03:10
 */
public interface ProviderNextStrategy {
    String strategyName();
    Provider next(List<Provider> providers);
}
