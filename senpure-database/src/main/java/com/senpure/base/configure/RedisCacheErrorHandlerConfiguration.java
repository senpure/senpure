package com.senpure.base.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@ConditionalOnClass({RedisTemplate.class})
public class RedisCacheErrorHandlerConfiguration extends  BaseConfiguration {


    @Bean
    public CacheErrorHandler cacheErrorHandler()
    {


        return  new CacheErrorGetHandler();
    }

    class  CacheErrorGetHandler extends SimpleCacheErrorHandler
    {
        @Override
        public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
            logger.error("redis 获取缓存出现异常 key ="+key,exception);
        }
    }
}
