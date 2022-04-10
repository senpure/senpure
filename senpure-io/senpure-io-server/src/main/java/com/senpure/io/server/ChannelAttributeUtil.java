package com.senpure.io.server;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class ChannelAttributeUtil {
    private static final String USER_ID = "userId";
    private static final String TOKEN = "token";
    private static final String USER_NAME = "userName";

    private static final String OFFLINE_HANDLER = "offlineHandler";
    private static final String CHANNEL_USER = "channelUser";

    private static final String REMOTE_SERVER_NAME = "remoteServerName";
    private static final String REMOTE_SERVER_KEY = "remoteServerKeyKey";
    private static final String LOCAL_SERVER_KEY = "localServerKeyKey";
    private static final String FRAMEWORK_KEY = "frameworkKey";
    private static final String FRAMEWORK_VERIFY_KEY = "frameworkVerifyKey";
    public static AttributeKey<Long> userIdKey = AttributeKey.valueOf(USER_ID);
    public static AttributeKey<Long> tokenKey = AttributeKey.valueOf(TOKEN);
    public static AttributeKey<String> userNameKey = AttributeKey.valueOf(USER_NAME);


    public static AttributeKey<String> remoteServerNameKey = AttributeKey.valueOf(REMOTE_SERVER_NAME);
    public static AttributeKey<String> remoteServerKeyKey = AttributeKey.valueOf(REMOTE_SERVER_KEY);
    public static AttributeKey<String> localServerKeyKey = AttributeKey.valueOf(LOCAL_SERVER_KEY);
    public static AttributeKey<Boolean> frameworkKey = AttributeKey.valueOf(FRAMEWORK_KEY);
    public static AttributeKey<Boolean> frameworkVerifyKey = AttributeKey.valueOf(FRAMEWORK_VERIFY_KEY);
    // public static AttributeKey<ChannelPlayer> channelPlayerKey = AttributeKey.valueOf(CHANNEL_USER);
    // public static AttributeKey<OffLineHandler> offlineHandlerKey = AttributeKey.valueOf(OFFLINE_HANDLER);

    public static void clear(ChannelHandlerContext ctx, AttributeKey<?> key) {
        ctx.channel().attr(key).set(null);
    }

    public static void clear(Channel channel, AttributeKey<?> key) {
        channel.attr(key).set(null);
    }


    public static void setFramework(Channel channel, boolean framework) {
        channel.attr(frameworkKey).set(framework);

    }

    public static boolean isFramework(Channel channel) {
        Boolean framework = channel.attr(frameworkKey).get();

        return framework != null && framework;
    }



    public static Long getUserId(ChannelHandlerContext ctx) {
        if (ctx == null) {
            return null;
        }
        return ctx.channel().attr(userIdKey).get();
    }


    public static void setToken(Channel channel, Long token) {
        channel.attr(tokenKey).set(token);
    }

    public static Long getToken(Channel channel) {
        return channel.attr(tokenKey).get();
    }

    public static void setUserId(Channel channel, Long playerId) {
        channel.attr(userIdKey).set(playerId);
    }

    public static void setUserId(ChannelHandlerContext ctx, Long playerId) {
        ctx.channel().attr(userIdKey).set(playerId);
    }


    public static void setUserName(Channel channel, String name) {

        channel.attr(userNameKey).set(name);

    }

    public static Long getUserId(Channel channel) {

        return channel.attr(userIdKey).get();

    }

    public static String getUserName(Channel channel) {

        return channel.attr(userNameKey).get();

    }

    public static void setRemoteServerName(Channel channel, String name) {
        channel.attr(remoteServerNameKey).set(name);
    }

    public static String getRemoteServerName(Channel channel) {
        return channel.attr(remoteServerNameKey).get();
    }


    public static void setRemoteServerKey(Channel channel, String serverKey) {

        channel.attr(remoteServerKeyKey).set(serverKey);

    }

    public static String getRemoteServerKey(Channel channel) {
        return channel.attr(remoteServerKeyKey).get();
    }

    public static String getLocalServerKey(Channel channel) {
        return channel.attr(localServerKeyKey).get();
    }

    public static void setLocalServerKey(Channel channel, String serverKey) {
        channel.attr(localServerKeyKey).set(serverKey);
    }


    public static <T> T get(ChannelHandlerContext ctx, AttributeKey<T> key) {
        return ctx.channel().attr(key).get();
    }

    public static <T> void set(Channel channel, AttributeKey<T> key, T value) {

        channel.attr(key).set(value);
    }

    public static <T> T get(Channel channel, AttributeKey<T> key) {

        return channel.attr(key).get();

    }

    public static <T> void set(ChannelHandlerContext ctx, AttributeKey<T> key, T value) {

        ctx.channel().attr(key).set(value);
    }
}
