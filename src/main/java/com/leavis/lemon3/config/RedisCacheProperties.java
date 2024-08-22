package com.leavis.lemon3.config;

import com.google.common.collect.Maps;
import java.time.Duration;
import java.util.Map;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: paynejlli
 * @Description: redis key过期时间配置
 * @Date: 2024/8/22 10:45
 */
@Getter
@Component
@ConfigurationProperties(prefix = "spring.redis-cache")
public class RedisCacheProperties {

    private final Map<String, Duration> keysTtl = Maps.newHashMap();

}