package com.senpure.io.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("server.io")
public class ServerProperties {

    /**
     * 服务名
     */
    private String serverName;
    /**
     * 服务实例唯一标识
     */

    private String serverKey;
    /**
     * 服务类型
     */

    private String serverType;
    /**
     * 服务扩展字段
     */

    private String serverOption;
    /**
     * 认证信息
     */
    private Verify verify = new Verify();
    private ConsumerProperties consumer = new ConsumerProperties();
    private GatewayProperties gateway = new GatewayProperties();
    private ProviderProperties provider = new ProviderProperties();


    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public ConsumerProperties getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerProperties consumer) {
        this.consumer = consumer;
    }

    public GatewayProperties getGateway() {
        return gateway;
    }

    public void setGateway(GatewayProperties gateway) {
        this.gateway = gateway;
    }

    public ProviderProperties getProvider() {
        return provider;
    }

    public void setProvider(ProviderProperties provider) {
        this.provider = provider;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getServerOption() {
        return serverOption;
    }

    public void setServerOption(String serverOption) {
        this.serverOption = serverOption;
    }

    public Verify getVerify() {
        return verify;
    }

    public void setVerify(Verify verify) {
        this.verify = verify;
    }

    public static class ProviderProperties {
        public static enum MODEL {
            /**
             * Provider 网关模式(主动连接网关)
             */
            GATEWAY,
            /**
             * Provider 直连模式(等待客户端连接)
             */
            CONSUMER
        }

        /**
         * 框架验证者
         */
        private boolean frameworkVerifyProvider;
        /**
         * 服务器名
         */
        private String readableName = "provider";
        /**
         * Provider 运行模式
         */
        private MODEL model = MODEL.GATEWAY;

        /**
         * 开启事件
         */
        private boolean enableEvent = true;
        /**
         * 处理事件的线程数
         */
        private int eventThreadPoolSize = 0;

        /**
         * 逻辑处理线程数
         */
        private int executorThreadPoolSize = 0;

        /**
         * 关联id与类名的包名多个用,逗号分割
         */
        private String idNamesPackage;


        private GatewayProperties gateway = new GatewayProperties();
        private ConsumerProperties consumer = new ConsumerProperties();

        public boolean isFrameworkVerifyProvider() {
            return frameworkVerifyProvider;
        }

        public void setFrameworkVerifyProvider(boolean frameworkVerifyProvider) {
            this.frameworkVerifyProvider = frameworkVerifyProvider;
        }


        public String getReadableName() {
            return readableName;
        }

        public void setReadableName(String readableName) {
            this.readableName = readableName;
        }

        public MODEL getModel() {
            return model;
        }

        public void setModel(MODEL model) {
            this.model = model;
        }

        public boolean isEnableEvent() {
            return enableEvent;
        }

        public void setEnableEvent(boolean enableEvent) {
            this.enableEvent = enableEvent;
        }

        public int getEventThreadPoolSize() {
            return eventThreadPoolSize;
        }

        public void setEventThreadPoolSize(int eventThreadPoolSize) {
            this.eventThreadPoolSize = eventThreadPoolSize;
        }


        public int getExecutorThreadPoolSize() {
            return executorThreadPoolSize;
        }

        public void setExecutorThreadPoolSize(int executorThreadPoolSize) {
            this.executorThreadPoolSize = executorThreadPoolSize;
        }


        public GatewayProperties getGateway() {
            return gateway;
        }

        public void setGateway(GatewayProperties gateway) {
            this.gateway = gateway;
        }

        public ConsumerProperties getConsumer() {
            return consumer;
        }

        public void setConsumer(ConsumerProperties consumer) {
            this.consumer = consumer;
        }

        public String getIdNamesPackage() {
            return idNamesPackage;
        }

        public void setIdNamesPackage(String idNamesPackage) {
            this.idNamesPackage = idNamesPackage;
        }

        public static class ConsumerProperties {
            /**
             * 绑定端口号
             */
            private int port = 2222;
            /**
             * 输出格式化
             */
            private boolean outFormat = true;
            /**
             * 输入格式化
             */
            private boolean inFormat = true;
            /**
             * 跳过心跳日志打印
             */
            private boolean skipHeart = false;
            /**
             * netty boosGroup 线程数
             */
            private int ioBossThreadPoolSize = 1;
            /**
             * netty workGroup 线程数
             */
            private int ioWorkThreadPoolSize = 0;
            /**
             * 开启ssl
             */
            private boolean ssl = false;
            /**
             * 是否开启心跳检查
             */
            private boolean enableHeartCheck = true;
            /**
             * 读心跳间隔毫秒
             */
            private long readerIdleTime = 15000;


            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public long getReaderIdleTime() {
                return readerIdleTime;
            }

            public void setReaderIdleTime(long readerIdleTime) {
                this.readerIdleTime = readerIdleTime;
            }

            public boolean isOutFormat() {
                return outFormat;
            }

            public void setOutFormat(boolean outFormat) {
                this.outFormat = outFormat;
            }

            public boolean isInFormat() {
                return inFormat;
            }

            public void setInFormat(boolean inFormat) {
                this.inFormat = inFormat;
            }

            public int getIoBossThreadPoolSize() {
                return ioBossThreadPoolSize;
            }

            public void setIoBossThreadPoolSize(int ioBossThreadPoolSize) {
                this.ioBossThreadPoolSize = ioBossThreadPoolSize;
            }

            public int getIoWorkThreadPoolSize() {
                return ioWorkThreadPoolSize;
            }

            public void setIoWorkThreadPoolSize(int ioWorkThreadPoolSize) {
                this.ioWorkThreadPoolSize = ioWorkThreadPoolSize;
            }

            public boolean isSsl() {
                return ssl;
            }

            public void setSsl(boolean ssl) {
                this.ssl = ssl;
            }

            public boolean isEnableHeartCheck() {
                return enableHeartCheck;
            }

            public void setEnableHeartCheck(boolean enableHeartCheck) {
                this.enableHeartCheck = enableHeartCheck;
            }

            public boolean isSkipHeart() {
                return skipHeart;
            }

            public void setSkipHeart(boolean skipHeart) {
                this.skipHeart = skipHeart;
            }
        }

        public static class GatewayProperties {
            public enum MODEL {
                /**
                 * 服务模式（通过注册中心查找服务）
                 */
                SERVER,
                /**
                 * 直连模式(直接连接ip地址) 该模式只能连接一个网关
                 */
                DIRECT
            }

            private MODEL model = MODEL.SERVER;
            /**
             * 网关服务名
             */
            private String name = "gateway";
            /**
             * 网关地址
             */
            private String host = "127.0.0.1";
            /**
             * 网关端口
             */
            private int port = 3333;


            /**
             * 连接网关失败后下一次连接间隔毫秒
             */
            private long connectFailInterval = 20000;
            /**
             * 与网关建立的channel 数量，多条channel在高并发下消息可能会有乱序问题
             */
            private int channel = 1;

            /**
             * 没有channel时下一个可用channel重发消息的时间限制
             */
            private int messageWaitSendTimeout = 10000;
            /**
             * 输出格式化
             */
            private boolean outFormat = true;
            /**
             * 输入格式化
             */
            private boolean inFormat = true;
            /**
             * 跳过心跳日志打印
             */
            private boolean skipHeart = false;
            /**
             * netty boosGroup 线程数
             */
            private int ioBossThreadPoolSize = 1;
            /**
             * netty workGroup 线程数
             */
            private int ioWorkThreadPoolSize = 0;
            /**
             * 开启ssl
             */
            private boolean ssl = false;
            /**
             * 是否开启心跳检查
             */
            private boolean enableHeartCheck = true;
            /**
             * 心跳写入间隔毫秒
             */
            private long writerIdleTime = 5000;

            public MODEL getModel() {
                return model;
            }

            public void setModel(MODEL model) {
                this.model = model;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public boolean isSkipHeart() {
                return skipHeart;
            }

            public void setSkipHeart(boolean skipHeart) {
                this.skipHeart = skipHeart;
            }

            public long getConnectFailInterval() {
                return connectFailInterval;
            }

            public void setConnectFailInterval(long connectFailInterval) {
                this.connectFailInterval = connectFailInterval;
            }

            public int getChannel() {
                return channel;
            }

            public void setChannel(int channel) {
                this.channel = channel;
            }

            public int getMessageWaitSendTimeout() {
                return messageWaitSendTimeout;
            }

            public void setMessageWaitSendTimeout(int messageWaitSendTimeout) {
                this.messageWaitSendTimeout = messageWaitSendTimeout;
            }

            public long getWriterIdleTime() {
                return writerIdleTime;
            }

            public void setWriterIdleTime(long writerIdleTime) {
                this.writerIdleTime = writerIdleTime;
            }

            public boolean isOutFormat() {
                return outFormat;
            }

            public void setOutFormat(boolean outFormat) {
                this.outFormat = outFormat;
            }

            public boolean isInFormat() {
                return inFormat;
            }

            public void setInFormat(boolean inFormat) {
                this.inFormat = inFormat;
            }

            public int getIoBossThreadPoolSize() {
                return ioBossThreadPoolSize;
            }

            public void setIoBossThreadPoolSize(int ioBossThreadPoolSize) {
                this.ioBossThreadPoolSize = ioBossThreadPoolSize;
            }

            public int getIoWorkThreadPoolSize() {
                return ioWorkThreadPoolSize;
            }

            public void setIoWorkThreadPoolSize(int ioWorkThreadPoolSize) {
                this.ioWorkThreadPoolSize = ioWorkThreadPoolSize;
            }

            public boolean isSsl() {
                return ssl;
            }

            public void setSsl(boolean ssl) {
                this.ssl = ssl;
            }

            public boolean isEnableHeartCheck() {
                return enableHeartCheck;
            }

            public void setEnableHeartCheck(boolean enableHeartCheck) {
                this.enableHeartCheck = enableHeartCheck;
            }
        }
    }

    public static class GatewayProperties {

        private ConsumerProperties consumer = new ConsumerProperties();
        private ProviderProperties provider = new ProviderProperties();

        /**
         * 服务名不是唯一
         */
        private String readableName = "gateway";
        /**
         * 工作线程数量
         */
        private int executorThreadPoolSize = 0;

        /**
         * 询问处理最多延迟毫秒
         */
        private long askMaxDelay = 1000;
        /**
         * 客户端登录消息id
         */
        private int csLoginMessageId = 1000101;
        /**
         * 服务器登录成功返回消息id
         */
        private int scLoginMessageId = 1000102;

        /**
         * 雪花算法的服务名
         */
        private String snowflakeDispatcherName = "dispatcher";
        /**
         * 没有找到雪花服务直接使用配置的dataCenterId与workId
         */
        private boolean notFoundSnowflakeUseCode = false;
        /**
         * 是否直接是配置文件中的雪花算法的dataCenterId与workId
         */
        private boolean snowflakeUseCode = false;
        /**
         * 雪花算法 dataCenterId
         */
        private int snowflakeDataCenterId = 0;
        /**
         * 雪花算法 workId
         */
        private int snowflakeWorkId = 0;
        /**
         * 框架认证使用简单认证
         */
        private boolean simpleVerify = false;
        /**
         * 认证token
         */
        private String simpleToken = "senpure.io.server.framework.simple.token";
        /**
         * 认证完成后的id
         */
        private long simpleUserId = 1;

        public ConsumerProperties getConsumer() {
            return consumer;
        }

        public void setConsumer(ConsumerProperties consumer) {
            this.consumer = consumer;
        }

        public ProviderProperties getProvider() {
            return provider;
        }

        public void setProvider(ProviderProperties provider) {
            this.provider = provider;
        }

        public String getReadableName() {
            return readableName;
        }

        public void setReadableName(String readableName) {
            this.readableName = readableName;
        }

        public long getSimpleUserId() {
            return simpleUserId;
        }

        public void setSimpleUserId(long simpleUserId) {
            this.simpleUserId = simpleUserId;
        }

        public int getExecutorThreadPoolSize() {
            return executorThreadPoolSize;
        }

        public void setExecutorThreadPoolSize(int executorThreadPoolSize) {
            this.executorThreadPoolSize = executorThreadPoolSize;
        }

        public int getCsLoginMessageId() {
            return csLoginMessageId;
        }

        public void setCsLoginMessageId(int csLoginMessageId) {
            this.csLoginMessageId = csLoginMessageId;
        }

        public int getScLoginMessageId() {
            return scLoginMessageId;
        }

        public void setScLoginMessageId(int scLoginMessageId) {
            this.scLoginMessageId = scLoginMessageId;
        }

        public String getSnowflakeDispatcherName() {
            return snowflakeDispatcherName;
        }

        public void setSnowflakeDispatcherName(String snowflakeDispatcherName) {
            this.snowflakeDispatcherName = snowflakeDispatcherName;
        }

        public boolean isNotFoundSnowflakeUseCode() {
            return notFoundSnowflakeUseCode;
        }

        public void setNotFoundSnowflakeUseCode(boolean notFoundSnowflakeUseCode) {
            this.notFoundSnowflakeUseCode = notFoundSnowflakeUseCode;
        }

        public boolean isSnowflakeUseCode() {
            return snowflakeUseCode;
        }

        public void setSnowflakeUseCode(boolean snowflakeUseCode) {
            this.snowflakeUseCode = snowflakeUseCode;
        }

        public int getSnowflakeDataCenterId() {
            return snowflakeDataCenterId;
        }

        public void setSnowflakeDataCenterId(int snowflakeDataCenterId) {
            this.snowflakeDataCenterId = snowflakeDataCenterId;
        }

        public int getSnowflakeWorkId() {
            return snowflakeWorkId;
        }

        public void setSnowflakeWorkId(int snowflakeWorkId) {
            this.snowflakeWorkId = snowflakeWorkId;
        }

        public long getAskMaxDelay() {
            return askMaxDelay;
        }

        public void setAskMaxDelay(long askMaxDelay) {
            this.askMaxDelay = askMaxDelay;
        }

        public boolean isSimpleVerify() {
            return simpleVerify;
        }

        public void setSimpleVerify(boolean simpleVerify) {
            this.simpleVerify = simpleVerify;
        }

        public String getSimpleToken() {
            return simpleToken;
        }

        public void setSimpleToken(String simpleToken) {
            this.simpleToken = simpleToken;
        }

        public static class ProviderProperties {

            /**
             * netty boosGroup 线程数
             */
            private int ioBossThreadPoolSize = 1;
            /**
             * netty workGroup 线程数
             */
            private int ioWorkThreadPoolSize = 0;

            /**
             * 监听端口号
             */
            private int port = 3333;

            /**
             * 开启ssl
             */
            private boolean ssl = false;
            /**
             * 是否开启心跳检查
             */
            private boolean enableHeartCheck = true;
            /**
             * 心跳读入间隔毫秒
             */
            private long readerIdleTime = 15000;
            /**
             * 跳过心跳日志打印
             */
            private boolean skipHeart = false;

            public int getIoBossThreadPoolSize() {
                return ioBossThreadPoolSize;
            }

            public void setIoBossThreadPoolSize(int ioBossThreadPoolSize) {
                this.ioBossThreadPoolSize = ioBossThreadPoolSize;
            }

            public int getIoWorkThreadPoolSize() {
                return ioWorkThreadPoolSize;
            }

            public void setIoWorkThreadPoolSize(int ioWorkThreadPoolSize) {
                this.ioWorkThreadPoolSize = ioWorkThreadPoolSize;
            }


            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public boolean isSsl() {
                return ssl;
            }

            public void setSsl(boolean ssl) {
                this.ssl = ssl;
            }

            public boolean isEnableHeartCheck() {
                return enableHeartCheck;
            }

            public void setEnableHeartCheck(boolean enableHeartCheck) {
                this.enableHeartCheck = enableHeartCheck;
            }

            public long getReaderIdleTime() {
                return readerIdleTime;
            }

            public void setReaderIdleTime(long readerIdleTime) {
                this.readerIdleTime = readerIdleTime;
            }

            public boolean isSkipHeart() {
                return skipHeart;
            }

            public void setSkipHeart(boolean skipHeart) {
                this.skipHeart = skipHeart;
            }

            @Override
            public String toString() {
                return "ProviderProperties{" +
                        "ioBossThreadPoolSize=" + ioBossThreadPoolSize +
                        ", ioWorkThreadPoolSize=" + ioWorkThreadPoolSize +
                        ", port=" + port +
                        ", ssl=" + ssl +
                        ", enableHeartCheck=" + enableHeartCheck +
                        ", readerIdleTime=" + readerIdleTime +
                        '}';
            }
        }

        public static class ConsumerProperties {

            /**
             * netty boosGroup 线程数
             */
            private int ioBossThreadPoolSize = 1;
            /**
             * netty workGroup 线程数
             */
            private int ioWorkThreadPoolSize = 0;

            /**
             * 监听端口号
             */
            private int port = 2222;

            /**
             * 开启ssl
             */
            private boolean ssl = false;
            /**
             * 是否开启心跳检查
             */
            private boolean enableHeartCheck = true;
            /**
             * 心跳读入间隔毫秒
             */
            private long readerIdleTime = 15000;
            /**
             * 跳过心跳日志打印
             */
            private boolean skipHeart = false;

            public int getIoBossThreadPoolSize() {
                return ioBossThreadPoolSize;
            }

            public void setIoBossThreadPoolSize(int ioBossThreadPoolSize) {
                this.ioBossThreadPoolSize = ioBossThreadPoolSize;
            }

            public int getIoWorkThreadPoolSize() {
                return ioWorkThreadPoolSize;
            }

            public void setIoWorkThreadPoolSize(int ioWorkThreadPoolSize) {
                this.ioWorkThreadPoolSize = ioWorkThreadPoolSize;
            }

            public boolean isSkipHeart() {
                return skipHeart;
            }

            public void setSkipHeart(boolean skipHeart) {
                this.skipHeart = skipHeart;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public boolean isSsl() {
                return ssl;
            }

            public void setSsl(boolean ssl) {
                this.ssl = ssl;
            }

            public boolean isEnableHeartCheck() {
                return enableHeartCheck;
            }

            public void setEnableHeartCheck(boolean enableHeartCheck) {
                this.enableHeartCheck = enableHeartCheck;
            }

            public long getReaderIdleTime() {
                return readerIdleTime;
            }

            public void setReaderIdleTime(long readerIdleTime) {
                this.readerIdleTime = readerIdleTime;
            }

            @Override
            public String toString() {
                return "ConsumerProperties{" +
                        "ioBossThreadPoolSize=" + ioBossThreadPoolSize +
                        ", ioWorkThreadPoolSize=" + ioWorkThreadPoolSize +
                        ", port=" + port +
                        ", ssl=" + ssl +
                        ", enableHeartCheck=" + enableHeartCheck +
                        ", readerIdleTime=" + readerIdleTime +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "GatewayProperties{" +
                    "consumer=" + consumer +
                    ", provider=" + provider +
                    ", readableName='" + readableName + '\'' +
                    ", executorThreadPoolSize=" + executorThreadPoolSize +
                    ", askMaxDelay=" + askMaxDelay +
                    ", csLoginMessageId=" + csLoginMessageId +
                    ", scLoginMessageId=" + scLoginMessageId +
                    ", snowflakeDispatcherName='" + snowflakeDispatcherName + '\'' +
                    ", notFoundSnowflakeUseCode=" + notFoundSnowflakeUseCode +
                    ", snowflakeUseCode=" + snowflakeUseCode +
                    ", snowflakeDataCenterId=" + snowflakeDataCenterId +
                    ", snowflakeWorkId=" + snowflakeWorkId +
                    '}';
        }
    }

    /**
     * 认证信息
     */
    public static class Verify {
        /**
         * 使用简单认证模式
         */
        private boolean simple;
        /**
         * 认证token
         */
        private String token = "senpure.io.server.framework.simple.token";
        /**
         * username
         */

        private String userName;
        /**
         * 账号类型
         */

        private String userType;
        /**
         * 账号密码
         */

        private String password;

        public boolean isSimple() {
            return simple;
        }

        public void setSimple(boolean simple) {
            this.simple = simple;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class ConsumerProperties {
        public enum MODEL {
            /**
             * 服务模式
             */
            SERVER,
            /**
             * 直连模式
             */
            DIRECT
        }

        /**
         * 服务器名
         */
        private String readableName = "consumer";
        /**
         * 连接模式
         */
        private MODEL model = MODEL.SERVER;

        /**
         * 连接的服务目标服务名
         */
        private String remoteName = "gateway";


        /**
         * 直连模式时 直接连接的端口号
         */
        private int remotePort = 2222;
        /**
         * 直连模式时 直接连接的地址
         */
        private String remoteHost = "127.0.0.1";
        /**
         * 自动连接服务
         */
        private boolean autoConnect = true;

        /**
         * 连接目标失败后下一次连接间隔毫秒(手动连接时自己处理)
         */
        private long connectFailInterval = 20000;
        /**
         * 与目标建立的channel 数量
         */
        private int remoteChannel = 1;

        /**
         * 同步请求超时时间
         */
        private int requestTimeout = 500;
        /**
         * 开启事件
         */
        private boolean enableEvent = false;

        /**
         * 处理事件的线程数
         */
        private int eventThreadPoolSize = 0;
        /**
         * netty workGroup 线程数
         */
        private int ioWorkThreadPoolSize = 1;
        /**
         * 逻辑处理线程数
         */
        private int executorThreadPoolSize = 0;

        /**
         * 没有channel时下一个可用channel重发消息的时间限制
         */
        private int messageWaitSendTimeout = 10000;

        /**
         * 输出格式化
         */
        private boolean outFormat = true;
        /**
         * 输入格式化
         */
        private boolean inFormat = true;
        /**
         * 开启ssl
         */
        private boolean ssl = false;

        /**
         * 是否开启心跳检查
         */
        private boolean enableHeartCheck = true;
        /**
         * 心跳写入间隔毫秒
         */
        private long writerIdleTime = 5000;
        /**
         * 跳过心跳日志打印
         */
        private boolean skipHeart = false;
        /**
         * 服务器返回错误消息id 多个用逗号,隔开
         */
        private String scErrorMessageId = "1000500";


        public String getReadableName() {
            return readableName;
        }

        public void setReadableName(String readableName) {
            this.readableName = readableName;
        }



        public MODEL getModel() {
            return model;
        }

        public void setModel(MODEL model) {
            this.model = model;
        }

        public String getRemoteName() {
            return remoteName;
        }

        public void setRemoteName(String remoteName) {
            this.remoteName = remoteName;
        }

        public int getRemotePort() {
            return remotePort;
        }

        public void setRemotePort(int remotePort) {
            this.remotePort = remotePort;
        }

        public String getRemoteHost() {
            return remoteHost;
        }

        public void setRemoteHost(String remoteHost) {
            this.remoteHost = remoteHost;
        }

        public boolean isAutoConnect() {
            return autoConnect;
        }

        public void setAutoConnect(boolean autoConnect) {
            this.autoConnect = autoConnect;
        }

        public long getConnectFailInterval() {
            return connectFailInterval;
        }

        public void setConnectFailInterval(long connectFailInterval) {
            this.connectFailInterval = connectFailInterval;
        }

        public int getRemoteChannel() {
            return remoteChannel;
        }

        public void setRemoteChannel(int remoteChannel) {
            this.remoteChannel = remoteChannel;
        }

        public int getRequestTimeout() {
            return requestTimeout;
        }

        public void setRequestTimeout(int requestTimeout) {
            this.requestTimeout = requestTimeout;
        }

        public boolean isEnableEvent() {
            return enableEvent;
        }

        public void setEnableEvent(boolean enableEvent) {
            this.enableEvent = enableEvent;
        }

        public int getEventThreadPoolSize() {
            return eventThreadPoolSize;
        }

        public void setEventThreadPoolSize(int eventThreadPoolSize) {
            this.eventThreadPoolSize = eventThreadPoolSize;
        }

        public int getIoWorkThreadPoolSize() {
            return ioWorkThreadPoolSize;
        }

        public void setIoWorkThreadPoolSize(int ioWorkThreadPoolSize) {
            this.ioWorkThreadPoolSize = ioWorkThreadPoolSize;
        }

        public int getExecutorThreadPoolSize() {
            return executorThreadPoolSize;
        }

        public void setExecutorThreadPoolSize(int executorThreadPoolSize) {
            this.executorThreadPoolSize = executorThreadPoolSize;
        }

        public int getMessageWaitSendTimeout() {
            return messageWaitSendTimeout;
        }

        public void setMessageWaitSendTimeout(int messageWaitSendTimeout) {
            this.messageWaitSendTimeout = messageWaitSendTimeout;
        }

        public boolean isSkipHeart() {
            return skipHeart;
        }

        public void setSkipHeart(boolean skipHeart) {
            this.skipHeart = skipHeart;
        }

        public boolean isOutFormat() {
            return outFormat;
        }

        public void setOutFormat(boolean outFormat) {
            this.outFormat = outFormat;
        }

        public boolean isInFormat() {
            return inFormat;
        }

        public void setInFormat(boolean inFormat) {
            this.inFormat = inFormat;
        }

        public boolean isSsl() {
            return ssl;
        }

        public void setSsl(boolean ssl) {
            this.ssl = ssl;
        }

        public boolean isEnableHeartCheck() {
            return enableHeartCheck;
        }

        public void setEnableHeartCheck(boolean enableHeartCheck) {
            this.enableHeartCheck = enableHeartCheck;
        }

        public long getWriterIdleTime() {
            return writerIdleTime;
        }

        public void setWriterIdleTime(long writerIdleTime) {
            this.writerIdleTime = writerIdleTime;
        }

        public String getScErrorMessageId() {
            return scErrorMessageId;
        }

        public void setScErrorMessageId(String scErrorMessageId) {
            this.scErrorMessageId = scErrorMessageId;
        }
    }
}
