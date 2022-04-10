package com.senpure.io.server.support.autoconfigure;

import com.senpure.base.autoconfigure.AbstractRootApplicationRunListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMetadataCompletionRunListener extends AbstractRootApplicationRunListener {
    public AbstractMetadataCompletionRunListener(SpringApplication springApplication, String[] args) {
        super(springApplication, args);
    }

    @Override
    public void rootEnvironmentPrepared(ConfigurableEnvironment environment) {
        Metadata metadata = metadata(environment);
        if (metadata == null) {
            return;
        }
        try {
            Class.forName("org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean");
        } catch (ClassNotFoundException e) {
            logger.warn("自动补全元数据目前只支持eureka注册中心，请您自己注意配置元数据");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        String eurekaMetadataPrefix = "eureka.instance.metadata-map.";
        for (Map.Entry<String, Object> entry : metadata.metadata.entrySet()) {
            String key = eurekaMetadataPrefix + entry.getKey();
            logger.debug("自动补全元数据 {} -> {}", key, entry.getValue());
            map.put(key, entry.getValue());
        }
        PropertySource<?> propertySource = new MapPropertySource(metadata.name + "MetadataMap", map);
        boolean add = true;
        for (PropertySource<?> temp : environment.getPropertySources()) {
            boolean has = false;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (temp.containsProperty(entry.getKey())) {
                    has = true;
                    break;
                }
            }
            if (has && temp instanceof OriginTrackedMapPropertySource) {
                add = false;
                environment.getPropertySources().addBefore(temp.getName(), propertySource);
                break;
            }
        }
        if (add) {
            environment.getPropertySources().addLast(propertySource);
        }
    }


    protected abstract Metadata metadata(ConfigurableEnvironment environment);

    static class Metadata {
        private final String name;
        private final Map<String, Object> metadata;

        public Metadata(String name) {
            this.name = name;
            this.metadata = new HashMap<>();
        }

        public Metadata(String name, Map<String, Object> metadata) {
            this.name = name;
            this.metadata = metadata;
        }

        public void addMetadata(String key, Object value) {
            metadata.put(key, value);
        }
    }
}
