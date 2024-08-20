package com.leavis.lemon3.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: paynejlli
 * @Description: 高德配置
 * @Date: 2024/8/20 11:53
 */
@Component
@Data
@ConfigurationProperties(prefix = "lemon.amp")
public class AmapConfig {

    private String domain;

    private String apiKey;

    private Integer timeout;

    private Integer readTime;
}
