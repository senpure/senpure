package com.senpure.javafx.autoconfigure;

import com.senpure.base.configure.BaseConfiguration;
import com.senpure.javafx.Javafx;
import com.senpure.javafx.JavafxProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;

/**
 * JavafxAutoConfiguration
 *
 * @author senpure
 * @time 2020-06-19 15:30:23
 */

public class JavafxAutoConfiguration extends BaseConfiguration implements ResourceLoaderAware {

    @Bean
    @ConfigurationProperties(prefix = "javafx")
    public JavafxProperties javafxProperties() {
        JavafxProperties javafxProperties = new JavafxProperties();
        Javafx.setJavafxProperties(javafxProperties);
        return javafxProperties;
    }


    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        Javafx.setResourceLoader(resourceLoader);

    }
}
