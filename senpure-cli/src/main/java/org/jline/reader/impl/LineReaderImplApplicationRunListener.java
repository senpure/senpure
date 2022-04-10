package org.jline.reader.impl;

import com.senpure.base.autoconfigure.AbstractRootApplicationRunListener;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;




/**
 *
 */
public class LineReaderImplApplicationRunListener extends AbstractRootApplicationRunListener {
    public LineReaderImplApplicationRunListener(SpringApplication springApplication, String[] args) {
        super(springApplication, args);
    }

    @Override
    public void rootStarting() {

    }

    @Override
    public void rootEnvironmentPrepared(ConfigurableEnvironment environment) {


    }

}
