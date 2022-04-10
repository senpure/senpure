package com.senpure.io.server.provider.gateway;

import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.server.remoting.AbstractServerInstanceMessageFrameSender;

public class Gateway extends AbstractServerInstanceMessageFrameSender {

    private boolean frameworkVerifyProviderPassed;

    private long nextConnectTime;
    private long streakFailTimes;

    public Gateway(TaskLoopGroup service) {
        super(service);
    }

    public boolean isFrameworkVerifyProviderPassed() {
        return frameworkVerifyProviderPassed;
    }

    public void setFrameworkVerifyProviderPassed(boolean frameworkVerifyProviderPassed) {
        this.frameworkVerifyProviderPassed = frameworkVerifyProviderPassed;
    }

    public void streakFailTimesIncr() {
        streakFailTimes++;
    }

    public long getNextConnectTime() {
        return nextConnectTime;
    }

    public void setNextConnectTime(long nextConnectTime) {
        this.nextConnectTime = nextConnectTime;
    }

    public long getStreakFailTimes() {
        return streakFailTimes;
    }

    public void setStreakFailTimes(long streakFailTimes) {
        this.streakFailTimes = streakFailTimes;
    }
}
