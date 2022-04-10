package com.senpure.io.server;


/**
 * Constant
 */
public class Constant {

    /**
     * 没有找到服务器
     */
    public final static String ERROR_NOT_FOUND_PROVIDER = "NOT_FOUND_PROVIDER";
    /**
     * 无法处理ASK请求
     */
    public final static String ERROR_NOT_HANDLE_VALUE_REQUEST = "NOT_HANDLE_VALUE_REQUEST";

    /**
     * 无法处理该请求,服务器没有注册该id
     */
    public final static String ERROR_NOT_HANDLE_REQUEST = "NOT_HANDLE_REQUEST";
    /**
     * 服务器错误
     */
    public final static String ERROR_PROVIDER_ERROR = "PROVIDER_ERROR";

    /**
     * 服务器错误
     */
    public final static String ERROR_GATEWAY_ERROR = "GATEWAY_ERROR";

    /**
     * 客户端错误
     */
    public final static String ERROR_CONSUMER_ERROR = "CONSUMER_ERROR";

    /**
     * 认证失败
     */
    public final static String ERROR_VERIFY_FAILURE = "VERIFY_FAILURE ";
    /**
     * 超时错误
     */
    public final static String ERROR_TIMEOUT = "TIMEOUT ";
    /**
     * 中断错误
     */
    public final static String ERROR_INTERRUPTED = "INTERRUPTED";

    /**
     * channel 不可用
     */
    public final static String ERROR_CHANNEL_NOT_AVAILABLE = "CHANNEL_NOT_AVAILABLE";


    public static final String BREAK_TYPE_ERROR = "ERROR";
    public static final String BREAK_TYPE_USER_OFFlINE = "USER_OFFLINE";
    public static final String BREAK_TYPE_USER_CHANGE = "USER_CHANGE";
    public static final String BREAK_TYPE_USER_LEAVE = "USER_LEAVE";


    public static final String GATEWAY_METADATA_PROVIDER_PORT = "provider.port";
    public static final String GATEWAY_METADATA_CONSUMER_PORT = "consumer.port";

    public static final int MAX_FRAMEWORK_USER_ID = 99999;
}
