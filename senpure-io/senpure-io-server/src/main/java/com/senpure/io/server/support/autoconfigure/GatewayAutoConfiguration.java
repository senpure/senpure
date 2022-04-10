package com.senpure.io.server.support.autoconfigure;

import com.senpure.base.util.Assert;
import com.senpure.base.util.IDGenerator;
import com.senpure.executor.DefaultTaskLoopGroup;
import com.senpure.executor.TaskLoopGroup;
import com.senpure.io.server.ServerProperties;
import com.senpure.io.server.gateway.GatewayMessageExecutor;
import com.senpure.io.server.support.GatewayServerStarter;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GatewayAutoConfiguration
 * <b>自动获取雪花数据时注意不在一个数据中心的机器，可能出现serverKey重复的情况 (本机ip地址相同)</b>
 *
 * @author senpure
 * @time 2019-02-28 16:52:21
 */
@ConditionalOnClass({CompositeDiscoveryClientAutoConfiguration.class,
        RestTemplateAutoConfiguration.class,
        LoadBalancerAutoConfiguration.class
})
@AutoConfigureAfter({CompositeDiscoveryClientAutoConfiguration.class,
        RestTemplateAutoConfiguration.class,
        LoadBalancerAutoConfiguration.class
}
)
public class GatewayAutoConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${server.port:0}")
    private int httpPort;
    private final RestTemplate restTemplate;
    private final ServerProperties properties;

    public GatewayAutoConfiguration(Environment env, ServerProperties properties,
                                    RestTemplateBuilder restTemplateBuilder,
                                    List<RestTemplateCustomizer> restTemplateCustomizers) {

        check(env, properties);
        this.properties = properties;
        restTemplate = restTemplateBuilder.build();

        // logger.info("======restTemplateCustomizers{}",restTemplateCustomizers);
        for (RestTemplateCustomizer customizer : restTemplateCustomizers) {
            customizer.customize(restTemplate);
        }
    }


    private void check(Environment env, ServerProperties properties) {
        if (!StringUtils.hasText(properties.getServerName())) {
            String name = env.getProperty("spring.application.name");
            if (StringUtils.hasText(name)) {
                logger.debug("使用 name {}", name);
                properties.setServerName(name);
            } else {
                logger.warn("spring.application.name 值为空");
            }
        }
        if (!StringUtils.hasText(properties.getServerName())) {
            properties.setServerName("gateway");
        }

        if (!StringUtils.hasText(properties.getServerType())) {
            properties.setServerType(properties.getServerName());
        }
        ServerProperties.GatewayProperties gateway = properties.getGateway();
        if (!StringUtils.hasText(gateway.getReadableName()) || gateway.getReadableName().equals(new ServerProperties.GatewayProperties().getReadableName())) {
            gateway.setReadableName(properties.getServerName());
        }
//
        //io *2 logic *1 综合1.5
        double size = Runtime.getRuntime().availableProcessors() * 1.5;
        int ioSize = (int) (size * 0.6);
        ioSize = Math.max(ioSize, 1);
        int logicSize = (int) (size * 0.4);
        logicSize = Math.max(logicSize, 1);
        if (gateway.getExecutorThreadPoolSize() < 1) {
            gateway.setExecutorThreadPoolSize(logicSize);
        }
        ServerProperties.GatewayProperties.ConsumerProperties gatewayConsumer = gateway.getConsumer();
        if (gatewayConsumer.getIoWorkThreadPoolSize() < 1) {
            int workSize = ioSize << 1;
            workSize = Math.max(workSize, 1);
            gatewayConsumer.setIoWorkThreadPoolSize(workSize);

        }
        ServerProperties.GatewayProperties.ProviderProperties gatewayProvider = gateway.getProvider();
        if (gatewayProvider.getIoWorkThreadPoolSize() < 1) {
            int workSize = ioSize << 1;
            workSize = Math.max(workSize, 1);
            gatewayProvider.setIoWorkThreadPoolSize(workSize);
        }

        logger.info(gateway.toString());
    }

    // @Autowired
    // private DiscoveryClient discoveryClient;

    // @Autowired
    // private RestTemplateBuilder restTemplateBuilder;
    @Resource
    private LoadBalancerClient loadBalancerClient;


    @Bean
    public GatewayMessageExecutor messageExecutor() {
        ServerProperties.GatewayProperties gateway = properties.getGateway();
        TaskLoopGroup service = new DefaultTaskLoopGroup(gateway.getExecutorThreadPoolSize(),
                new DefaultThreadFactory(properties.getServerName() + "-executor"));
        GatewayMessageExecutor messageExecutor = new GatewayMessageExecutor(service, new IDGenerator(gateway.getSnowflakeDataCenterId(), gateway.getSnowflakeWorkId()));
        messageExecutor.setCsLoginMessageId(gateway.getCsLoginMessageId());
        messageExecutor.setScLoginMessageId(gateway.getScLoginMessageId());
        messageExecutor.setGatewayProperties(properties.getGateway());
        messageExecutor.init();
        return messageExecutor;
    }


    @Bean

    public GatewayServerStarter gatewayServer(GatewayMessageExecutor messageExecutor) {

        if (!properties.getGateway().isSnowflakeUseCode()) {
            logger.debug("通过远程服务[{}]获取雪花算法参数", properties.getGateway().getSnowflakeDispatcherName());
            ServiceInstance serviceInstance = loadBalancerClient.choose(properties.getGateway().getSnowflakeDispatcherName());
            boolean readRemote = true;
            if (serviceInstance == null) {
                if (!properties.getGateway().isNotFoundSnowflakeUseCode()) {
                    Assert.error(properties.getGateway().getSnowflakeDispatcherName() + "雪花调度服务没有启动");
                } else {
                    logger.warn("雪花调度服务没有启动 降级使用配置数据  dataCenterId {} workId {}", properties.getGateway().getSnowflakeDataCenterId(), properties.getGateway().getSnowflakeWorkId());
                    readRemote = false;
                }
                // logger.error("{} 雪花调度服务没有启动", properties.getGateway().getSnowflakeDispatcherName());
            }
            if (readRemote) {

                String url = "http://" + properties.getGateway().getSnowflakeDispatcherName() + "/snowflake/dispatch?serverName={serverName}&serverKey={serverKey}";
                Map<String, String> params = new LinkedHashMap<>();
                params.put("serverName", properties.getServerName());
                String serverKey;
//            if (AppEvn.classInJar(AppEvn.getStartClass())) {
//                serverKey = AppEvn.getClassPath(AppEvn.getStartClass());
//            } else {
//                serverKey = AppEvn.getClassRootPath();
//            }
                InetAddress inetAddress = getLocalHostLANAddress();
                if (inetAddress == null) {
                    Assert.error("本机地址为空");

                }
                serverKey = properties.getServerName() + " " + inetAddress.getHostAddress() + ":" + (httpPort > 0 ? httpPort : properties.getGateway().getConsumer().getPort());
                params.put("serverKey", serverKey);
                //  ObjectNode nodes = restTemplate.getForObject(url, ObjectNode.class, params);
                // logger.debug("雪花调度返回 {}", JSON.toJSONString(nodes));
                Result result = restTemplate.getForObject(url, Result.class, params);

                logger.debug("雪花调度返回 {}", result);
                if (result == null) {
                    Assert.error(properties.getGateway().getSnowflakeDispatcherName() + "雪花调度服务出错 : result is null");

                }
                if (result.getCode() != 1) {
                    Assert.error(properties.getGateway().getSnowflakeDispatcherName() + "雪花调度服务出错 :" + result.message + (result.getValidators() == null ? "" : result.getValidators().toString()));
                }
                properties.getGateway().setSnowflakeDataCenterId(result.getServerCenterAndWork().getCenterId());
                properties.getGateway().setSnowflakeWorkId(result.getServerCenterAndWork().getWorkId());
            }
        }
        GatewayServerStarter gatewayServer = new GatewayServerStarter();
        gatewayServer.setProperties(properties);
        gatewayServer.start(messageExecutor);
        return gatewayServer;
    }

    static class Result {
        private int code;
        private String message;
        private ServerCenterAndWork serverCenterAndWork;
        private Map<String, String> validators;

        public Map<String, String> getValidators() {
            return validators;
        }

        public void setValidators(Map<String, String> validators) {
            this.validators = validators;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ServerCenterAndWork getServerCenterAndWork() {
            return serverCenterAndWork;
        }

        public void setServerCenterAndWork(ServerCenterAndWork serverCenterAndWork) {
            this.serverCenterAndWork = serverCenterAndWork;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", serverCenterAndWork=" + serverCenterAndWork +
                    ", validators=" + validators +
                    '}';
        }
    }

    static class ServerCenterAndWork {
        private String serverName;

        private String serverKey;

        private Integer centerId;

        private Integer workId;

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getServerKey() {
            return serverKey;
        }

        public void setServerKey(String serverKey) {
            this.serverKey = serverKey;
        }

        public Integer getCenterId() {
            return centerId;
        }

        public void setCenterId(Integer centerId) {
            this.centerId = centerId;
        }

        public Integer getWorkId() {
            return workId;
        }

        public void setWorkId(Integer workId) {
            this.workId = workId;
        }

        @Override
        public String toString() {
            return "ServerCenterAndWork{" +
                    "serverName='" + serverName + '\'' +
                    ", serverKey='" + serverKey + '\'' +
                    ", centerId=" + centerId +
                    ", workId=" + workId +
                    '}';
        }
    }

    public static InetAddress getLocalHostLANAddress() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            return InetAddress.getLocalHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws UnknownHostException {
        String str = " {\"code\":104,\"validators\":{\"serverName\":\"不能为空\"},\"getValue\":\"输入格式错误\"}";


        System.out.println(getLocalHostLANAddress().getHostAddress());
        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
        System.out.println(jdkSuppliedAddress);
    }
}
