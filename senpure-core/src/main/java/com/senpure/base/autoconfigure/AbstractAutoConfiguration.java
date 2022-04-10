package com.senpure.base.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractAutoConfiguration
 *
 * @author senpure
 * @time 2020-05-13 14:33:38
 */
public abstract class AbstractAutoConfiguration {
    protected Logger logger;
    public AbstractAutoConfiguration() {
        logger = LoggerFactory.getLogger(getClass());
    }
}
