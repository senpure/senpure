package com.senpure.io.generator.executor;

/**
 * CheckError
 *
 * @author senpure
 * @time 2019-07-11 16:39:38
 */
public class CheckException extends RuntimeException {
    public CheckException() {
        super();
    }

    public CheckException(String message) {
        super(message);
    }
}
