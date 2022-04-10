package com.senpure.io.server.gateway.provider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 选取服务实例分数最高的一个实例
 *
 * @author senpure
 * @time 2020-06-05 15:23:27
 */
public class ProviderDefaultNextStrategy implements ProviderNextStrategy {
    private final Comparator<Provider> comparator=new StatisticComparator();
    @Override
    public String strategyName() {
        return "default";
    }


    @Override
    public Provider next(List<Provider> providers) {
        List<Provider> sortedProviders = new ArrayList<>(providers);
        sortedProviders.sort(comparator);
        return sortedProviders.get(0);
    }
    static class  StatisticComparator implements  Comparator<Provider>{

        @Override
        public int compare(Provider x, Provider y) {
            int result= Integer.compare(y.getStatistic().getScore(), x.getStatistic().getScore());
            if (result == 0) {
                result = Integer.compare(y.getStatistic().getConsumerCount(), x.getStatistic().getConsumerCount());
            }
            return result;
        }
    }
}
