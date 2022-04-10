package com.senpure.base.configure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseConfiguration
 *
 * @author senpure
 * @time 2020-05-13 14:33:38
 */
public class BaseConfiguration {
    protected Logger logger;
    public BaseConfiguration() {
        logger = LoggerFactory.getLogger(getClass());
    }
}
