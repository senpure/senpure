package com.senpure.io.server.consumer;

import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.server.remoting.AbstractServerInstanceMessageFrameSender;

public class Provider extends AbstractServerInstanceMessageFrameSender {
    private long nextConnectTime;
    private long streakFailTimes;
    private String remoteHost;
    private int  remotePort;

    public Provider(TaskLoopGroup service) {
        super(service);
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

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }
}
