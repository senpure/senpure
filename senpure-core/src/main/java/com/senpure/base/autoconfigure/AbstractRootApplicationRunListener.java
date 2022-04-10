package com.senpure.base.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * 当存在d多个上下文是指执行最初的的上下文监听
 *
 * @author senpure
 * @time 2020-05-08 18:55:33
 */
public abstract class AbstractRootApplicationRunListener implements SpringApplicationRunListener {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final Map<Class<?>, Boolean> flagMap = new HashMap<>();
    private boolean root;
    protected final SpringApplication springApplication;

    protected final String source;

    public AbstractRootApplicationRunListener(SpringApplication springApplication, String[] args) {
        this.springApplication = springApplication;
        Class<?> clazz = getClass();
        boolean flag = flagMap.getOrDefault(clazz, false);
        //System.out.println(clazz + " flag = " + flag);
        if (springApplication.getAllSources().size() > 0) {
            source = springApplication.getAllSources().iterator().next().toString();
        } else {
            source = "unknown";
        }
        if (!flag) {
            flagMap.putIfAbsent(clazz, true);
            root = true;
        }

    }

    public final boolean isRoot() {
        return root;
    }


    public abstract void rootStarting();

    @Override
    public void starting() {
        if (root) {
            rootStarting();
        }
    }

    public abstract void rootEnvironmentPrepared(ConfigurableEnvironment environment);


    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        if (root) {
            rootEnvironmentPrepared(environment);
        }
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }
}
