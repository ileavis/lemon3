package com.leavis.lemon3.config;

import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.Map;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: paynejlli
 * @Description: redis key过期时间配置
 * @Date: 2024/8/22 10:45
 */
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.redis-cache")
@RefreshScope
public class RedisCacheProperties {

    private final Map<String, Duration> keysTtl = Maps.newHashMap();

}