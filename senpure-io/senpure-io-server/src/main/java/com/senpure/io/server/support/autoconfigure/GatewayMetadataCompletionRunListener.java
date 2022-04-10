package com.senpure.io.server.support.autoconfigure;

import com.senpure.base.AppEvn;
import com.senpure.base.autoconfigure.AbstractRootApplicationRunListener;
import com.senpure.base.util.RandomUtil;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.support.annotation.EnableGateway;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GatewayMetadataCompletionConfiguration
 *
 * @author senpure
 * @time 2020-05-07 18:31:27
 */
public class GatewayMetadataCompletionRunListener extends AbstractRootApplicationRunListener {

    public GatewayMetadataCompletionRunListener(SpringApplication springApplication, String[] args) {
        super(springApplication, args);
    }

    @Override
    public void rootStarting() {

    }


    @Override
    public void rootEnvironmentPrepared(ConfigurableEnvironment environment) {
//            ConfigurationPropertySourcesPropertySource temp = null;
//            for (PropertySource<?> propertySource : environment.getPropertySources()) {
//                if (propertySource instanceof ConfigurationPropertySourcesPropertySource) {
//                    temp = (ConfigurationPropertySourcesPropertySource) propertySource;
//                    break;
//                }
//            }
//            if (temp == null) {
//                logger.warn("没有找到ConfigurationPropertySourcesPropertySource，可能是springboot版本问题，无法自动补全元数据，请您自己注意配置元数据");
//                return;
//            }
//            SpringConfigurationPropertySource source=  SpringConfigurationPropertySource.from(temp);
//            ConfigurationPropertyName configurationPropertyName = ConfigurationPropertyName.of("eureka.instance.metadata-map.cs-port");
//            source.getConfigurationProperty(configurationPropertyName).getValue();

        Class<?> bootClass = AppEvn.getStartClass();
        EnableGateway enableGateway = bootClass.getAnnotation(EnableGateway.class);
        if (enableGateway != null) {
            Integer consumerPort = environment.getProperty("server.io.gateway.consumer.port", Integer.class);
            Integer providerPort = environment.getProperty("server.io.gateway.provider.port", Integer.class);
            ServerProperties serverProperties = new ServerProperties();
            ServerProperties.GatewayProperties gateway = serverProperties.getGateway();
            Map<String, Object> map = new HashMap<>();
            if (consumerPort == null) {
                consumerPort = gateway.getConsumer().getPort();
            }
            if (providerPort == null) {
                providerPort = gateway.getProvider().getPort();
            }
            if (consumerPort == 0) {

                List<Integer> csPorts = new ArrayList<>();
                consumerPort = gateway.getConsumer().getPort();
                for (int i = 0; i < 5; i++) {
                    csPorts.add(consumerPort + i);
                }
                consumerPort = getPort(csPorts);
                map.put("server.io.gateway.consumer.port", consumerPort);
            }
            if (providerPort == 0) {
                List<Integer> scPorts = new ArrayList<>();
                providerPort = gateway.getProvider().getPort();
                for (int i = 0; i < 5; i++) {
                    scPorts.add(providerPort + i);
                }
                providerPort = getPort(scPorts);
                map.put("server.io.gateway.provider.port", providerPort);
            }
            try {
                Class.forName("org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean");
            } catch (ClassNotFoundException e) {
                logger.warn("1.自动补全元数据目前只支持eureka注册中心，请您自己注意配置元数据");
                logger.warn("2.自动补全元数据目前只支持eureka注册中心，请您自己注意配置元数据");
                logger.warn("3.自动补全元数据目前只支持eureka注册中心，请您自己注意配置元数据");
                logger.warn("4.自动补全元数据目前只支持eureka注册中心，请您自己注意配置元数据");
                logger.warn("5.自动补全元数据目前只支持eureka注册中心，请您自己注意配置元数据");
                return;
            }

            map.put("eureka.instance.metadata-map[consumer.port]", consumerPort);
            map.put("eureka.instance.metadata-map[provider.port]", providerPort);
            PropertySource<?> propertySource = new MapPropertySource("gatewayMetadataMap", map);
            environment.getPropertySources().addFirst(propertySource);

        }
    }

    private int getPort(List<Integer> ports) {
        for (Integer port : ports) {
            if (!isPortUsing("127.0.0.1", port)) {
                return port;
            }
        }
        return getPort();
    }


    private int getPort() {
        int port;
        do {
            //ServerSocket serverSocket =  new ServerSocket(0);
            //serverSocket.getLocalPort();
            port = RandomUtil.random(1, 65536);
        } while (isPortUsing("127.0.0.1", port));

        return port;

    }

    public static boolean isPortUsing(String host, int port) {
        boolean flag = false;

        try {
            InetAddress Address = InetAddress.getByName(host);
            new Socket(Address, port).close();  //建立一个Socket连接
            flag = true;
        } catch (Exception ignored) {
        }
        return flag;
    }
}
