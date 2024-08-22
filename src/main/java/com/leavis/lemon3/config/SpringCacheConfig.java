package com.leavis.lemon3.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.Map;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @Author: paynejlli
 * @Description: spring cache配置
 * @Date: 2024/8/22 10:18
 */
@Configuration
public class SpringCacheConfig {

    @Resource
    private RedisCacheProperties redisCacheProperties;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)).disableCachingNullValues();

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);

        ImmutableSet.Builder<String> cacheNames = ImmutableSet.builder();
        ImmutableMap.Builder<String, RedisCacheConfiguration> cacheConfig = ImmutableMap.builder();
        for (Map.Entry<String, Duration> entry : redisCacheProperties.getKeysTtl().entrySet()) {
            cacheNames.add(entry.getKey());
            cacheConfig.put(entry.getKey(), defaultCacheConfig.entryTtl(entry.getValue()));
        }

        return RedisCacheManager.builder(redisCacheWriter)
                .cacheDefaults(defaultCacheConfig)
                .initialCacheNames(cacheNames.build())
                .withInitialCacheConfigurations(cacheConfig.build())
                .build();
    }
}
