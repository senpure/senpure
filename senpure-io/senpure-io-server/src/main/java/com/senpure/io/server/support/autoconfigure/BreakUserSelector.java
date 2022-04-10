package com.senpure.io.server.support.autoconfigure;


import com.senpure.io.server.support.annotation.EnableProvider;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotationMetadata;

/**
 * BreakUserSelector<br>
 * 有些服务器需要自己处理断开，如发送退出事件等
 *
 * @author senpure
 * @time 2019-03-04 11:50:05
 */
public class BreakUserSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        MergedAnnotation<EnableProvider> annotation = importingClassMetadata.getAnnotations().get(EnableProvider.class);
        if (annotation.isPresent()) {
            if (annotation.getBoolean("breakUser")) {
               // return new String[]{CSBreakUserGatewayMessageHandler.class.getName()};
            }
        }
        return new String[0];
    }
}
