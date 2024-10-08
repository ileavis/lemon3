package com.leavis.lemon3.client.config;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: paynejlli
 * @Description: 高德配置
 * @Date: 2024/8/20 11:53
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "lemon.amp")
//@RefreshScope 动态刷新时使用
public class AmapConfig {

    private String domain;

    private String apiKey;

    private Duration timeout;

    private Duration readTime;
}
