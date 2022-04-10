package com.senpure.javafx;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * JavafxBoot
 *
 * @author senpure
 * @time 2020-06-19 14:48:00
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
public @interface JavafxBoot {
    @AliasFor(annotation = SpringBootApplication.class)
    Class<?>[] exclude() default {};


    @AliasFor(annotation = SpringBootApplication.class)
    String[] excludeName() default {};


    @AliasFor(annotation = SpringBootApplication.class)
    String[] scanBasePackages() default {};

    @AliasFor(annotation = SpringBootApplication.class)
    Class<?>[] scanBasePackageClasses() default {};


    @AliasFor(annotation = SpringBootApplication.class)
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;


    @AliasFor(annotation = SpringBootApplication.class)
    boolean proxyBeanMethods() default true;

}
