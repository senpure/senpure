package com.senpure.io.server.gateway;

import com.senpure.io.server.gateway.provider.Provider;
import com.senpure.io.server.gateway.provider.ProviderManager;

/**
 * WaitAskTask
 *
 * @author senpure
 * @time 2018-11-01 18:03:18
 */
public class WaitAskTask {


    /**
     * 询问key
     */
    private long askToken;

    private byte[] value;

    private long startTime;
    private long answerTime;
    /**
     * 询问了多少个服务器
     */
    private int askTimes;
    /**
     * 应答了多少个服务器
     */
    private int answerTimes;
    private long maxDelay = 3000;

    private int fromMessageId;
    private int requestId;
    private Provider provider;

    private ProviderManager providerManager;
    private GatewaySendProviderMessage message;


    public WaitAskTask(long maxDelay) {
        this.maxDelay = maxDelay;
        startTime = System.currentTimeMillis();
    }

    public synchronized void answer(ProviderManager providerManager, Provider provider, boolean canHandle) {
        answerTimes++;
        if (canHandle) {
            if (this.provider != null) {
                return;
            }
            this.providerManager = providerManager;
            this.provider = provider;
        }
    }

    public long getToken() {
        return message.token();
    }

    public void sendMessage() {
        providerManager.relationAndSendMessage(provider, message);

    }

    public boolean cancel() {
        if (provider != null) {
            return false;
        }
        return System.currentTimeMillis() - startTime > maxDelay;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(long answerTime) {
        this.answerTime = answerTime;
    }

    public long getAskToken() {
        return askToken;
    }

    public void setAskToken(long askToken) {
        this.askToken = askToken;
    }


    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


    public GatewaySendProviderMessage getMessage() {
        return message;
    }

    public void setMessage(GatewaySendProviderMessage message) {
        this.message = message;
    }

    public int getAnswerTimes() {
        return answerTimes;
    }

    public void setAnswerTimes(int answerTimes) {
        this.answerTimes = answerTimes;
    }

    public int getAskTimes() {
        return askTimes;
    }

    public void setAskTimes(int askTimes) {
        this.askTimes = askTimes;
    }

    public int getFromMessageId() {
        return fromMessageId;
    }

    public void setFromMessageId(int fromMessageId) {
        this.fromMessageId = fromMessageId;
    }

    public long getMaxDelay() {
        return maxDelay;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
