package com.senpure.base.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;


public class Spring implements ApplicationContextAware {

    protected static Logger logger = LogManager.getLogger(Spring.class);
    private static ConfigurableApplicationContext act;


    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {

        regApplicationContext(context);

    }

    public static void regApplicationContext(ApplicationContext context) {
        logger.info("regApplicationContext:{}  {}", context.getApplicationName(), context.getClass());

        if (act == null) {
            // logger.info("Spring 获取ApplicationContext上下文:applicationName:" + context.getApplicationName() + ",displayName:" + context.getDisplayName() + ",id:" + context.getId());
            act = (ConfigurableApplicationContext) context;
        }
    }

    public static void regBean(Object bean) {
        act.getBeanFactory().registerSingleton(bean.getClass().getName(), bean);
    }

    public static void regBean(String name, Object bean) {
        act.getBeanFactory().registerSingleton(name, bean);
    }

    public static void regBean(Class<?> clazz) {

        regBean(clazz.getName(), clazz);
    }

    public static void regBean(String name, Class<?> clazz) {

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) act.getBeanFactory();

        defaultListableBeanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
    }

    public static boolean isRunning() {
        return act.isRunning();
    }

    public static <T> T getBean(Class<T> type) {

        return act.getBean(type);
    }

    public static Object getBean(String name) {

        return act.getBean(name);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {

        return act.getBeansOfType(type);
    }

    public static void exit(ExitCodeGenerator... exitCodeGenerators) {
        if (act != null) {
            SpringApplication.exit(act, exitCodeGenerators);
        } else {
            System.exit(0);

        }

    }


}
