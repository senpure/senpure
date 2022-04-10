package com.senpure.io.server.support.annotation;

import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.support.autoconfigure.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * EnableProducer
 *
 * @author senpure
 * @time 2020-01-09 15:14:07
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@EnableConfigurationProperties({ServerProperties.class})
@Import({
        ProviderAutoConfiguration.class,
        ProviderGatewayAutoConfiguration.class,
        ProviderConsumerAutoConfiguration.class,
        ProviderMessageHandlerAutoConfiguration.class

})
public @interface EnableProvider {
//    /**
//     * 注册断开处理器
//     *
//     * @return
//     */
//    @AliasFor("value")
//    boolean breakUser() default true;
//
//    @AliasFor(value = "breakUser")
//    boolean value() default true;


}
