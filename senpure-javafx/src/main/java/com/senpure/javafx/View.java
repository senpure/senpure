package com.senpure.javafx;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * FXMLView
 *
 * @author senpure
 * @time 2020-06-18 16:11:43
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Lazy
public @interface View {

    @AliasFor("fxml")
    String value() default "";

    @AliasFor("value")
    String fxml() default "";

    String title() default "";

    @AliasFor(value = "value", annotation = Lazy.class)
    boolean lazy() default true;

    String[] css() default {};


    /**
     * 国际化basename
     *
     * @return
     */
    String[] basenames() default {};
}
