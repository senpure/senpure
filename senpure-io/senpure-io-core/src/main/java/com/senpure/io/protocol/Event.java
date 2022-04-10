package com.senpure.io.protocol;

/**
 * Event
 *
 * @author senpure
 * @time 2020-01-06 10:52:29
 */
public interface Event extends Bean {
    /**
     * 事件唯一标识
     *
     */
    int eventId();
}
