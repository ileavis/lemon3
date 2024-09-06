package com.leavis.lemon3.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: paynejlli
 * @Description: netty服务器配置
 * @Date: 2024/9/4 17:48
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "lemon.netty")
@RefreshScope
public class NettyConfig {

    /**
     * 服务器端口
     */
    private Integer port;

    /**
     * 心跳超时时间,单位为秒
     */
    private Long heartBeatTimeout;

    private Ssl ssl;

    @Data
    public static class Ssl {

        /**
         * 是否启用ssl
         */
        private Boolean enabled;

        /**
         * ssl类型
         */
        private String type;

        /**
         * 密钥库文件
         */
        private String keyStore;

        /**
         * 密钥库密码
         */
        private String keyStorePassword;
    }
}
