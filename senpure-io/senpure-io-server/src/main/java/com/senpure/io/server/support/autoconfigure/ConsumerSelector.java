package com.senpure.io.server.support.autoconfigure;


import com.senpure.io.server.consumer.handler.SCHeartMessageHandler;
import com.senpure.io.server.consumer.handler.SCFrameworkErrorMessageHandler;
import com.senpure.io.server.support.annotation.EnableConsumer;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * ConsumerSelector
 *
 * @author senpure
 * @time 2019-07-02 17:00:11
 */
public class ConsumerSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {

        Class<?> annotationType = EnableConsumer.class;
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(
                annotationType.getName(), false));
        List<String> values = new ArrayList<>(16);
        if (attributes.getBoolean("innerError")) {
            // return new String[]{SCInnerErrorMessageHandler.class.getName()};
            values.add(SCFrameworkErrorMessageHandler.class.getName());
        }
        if (attributes.getBoolean("heart")) {
            values.add(SCHeartMessageHandler.class.getName());
        }
        return values.toArray(new String[values.size()]);
    }
}
