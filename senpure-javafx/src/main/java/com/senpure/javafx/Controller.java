package com.senpure.javafx;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Controller
 *
 * @author senpure
 * @time 2020-06-22 10:52:59
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Lazy
public @interface Controller {
    @AliasFor(value="value", annotation = Lazy.class)
    boolean lazy() default true;
}
