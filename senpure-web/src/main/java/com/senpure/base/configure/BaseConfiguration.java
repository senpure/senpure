package com.senpure.base.configure;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseConfiguration {

    protected Logger logger;

    public BaseConfiguration() {
        logger= LoggerFactory.getLogger(getClass());
    }
}
