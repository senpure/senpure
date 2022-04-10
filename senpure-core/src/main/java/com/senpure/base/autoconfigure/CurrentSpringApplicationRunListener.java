package com.senpure.base.autoconfigure;

import com.senpure.base.AppEvn;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

@Order(1)
public class CurrentSpringApplicationRunListener extends AbstractRootApplicationRunListener {

    public CurrentSpringApplicationRunListener(SpringApplication springApplication, String[] args) {
        super(springApplication, args);
    }

    @Override
    public void rootStarting() {

        if (isRoot()) {
            Class<?> clazz = AppEvn.getStartClass();
            if (clazz == null) {
                Class<?> bootClass = null;
                if (springApplication.getAllSources().size() > 0) {
                    Object object = springApplication.getAllSources().iterator().next();
                    if (object instanceof Class) {
                        bootClass = (Class<?>) springApplication.getAllSources().iterator().next();
                    }
                }
                clazz = bootClass;
                if (clazz == null) {
                    StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
                    StackTraceElement stack = stacks[stacks.length - 1];
                    try {
                        clazz = Class.forName(stack.getClassName());
                        AppEvn.markStartClass(clazz);

                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }
            AppEvn.markClassRootPath(clazz);
        }
    }

    @Override
    public void rootEnvironmentPrepared(ConfigurableEnvironment environment) {
        String value = environment.getProperty("spring.output.ansi.enabled", String.class);
        logger.trace("spring.output.ansi.enabled={}",value);
        if (value == null || !value.equalsIgnoreCase(AnsiOutput.Enabled.NEVER.name())) {
            Boolean install=environment.getProperty("senpure.ansi-console.install", Boolean.class);
            logger.trace("senpure.ansi-console.install={}",install);
            if (install == null || install) {
                AppEvn.installAnsiConsole(AppEvn.getStartClass());
            }

        }

    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        if (isRoot()) {
            logger.info("springApplication bootClass {}", AppEvn.getStartClass().getName());
        }
    }
}
