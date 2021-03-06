package com.senpure.base.menu;

import java.lang.annotation.*;


@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MenuGenerator {

    int id();

    int parentId() default 0;

    String text();

    String uri() default "";

    int sort() default 0;

    String i18nKey() default "";
    String icon() default "glyphicon glyphicon-th faa-float";
    String description() default "";
    String config() default"";
}
