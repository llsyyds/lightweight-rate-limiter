package io.llsfish.ratelimiter.common.config;

import io.llsfish.ratelimiter.common.constant.Constant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;


/**
 * @author Redick01
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = Constant.CONFIG_PREFIX)
public class RtProperties {

    private String configType = "yml";

    private Nacos nacos;

    private Boolean enableMonitor = false;

    private Integer monitorInterval = 5;

    //重置rateLimitermetrics的时间
    private Integer recoverInterval = 60;

    private List<RateLimiterConfigProperties> rateLimiterConfigs;

    private RedisConfig redisConfig;

    /**
     * Nacos config.
     */
    @Data
    public static class Nacos {

        private String dataId;

        private String group;
    }

    /**
     * Redis config.
     */
    @Data
    public static class RedisConfig {

        private Integer database = 0;

        /**
         * If it is cluster or sentinel mode, separated with `;`.
         */
        private String url;

        /**
         * the password.
         */
        private String password;

        /**
         * 连接池可以创建的最大连接数。一旦这个数量被达到，连接池将不会创建更多的连接
         */
        private int maxActive = 8;

        /**
         * 连接池中可以保持的最大的空闲连接数。超过这个数的空闲连接将被释放
         */
        private int maxIdle = 8;

        /**
         * 连接池中至少保持的空闲连接数。连接池会确保至少有这么多的空闲连接可用
         * 默认为0
         */
        private int minIdle;


        /**
         * Maximum amount of time a connection allocation should block before throwing an
         * exception when the pool is exhausted. Use a negative value to block
         * indefinitely.
         */
        private Duration maxWait = Duration.ofMillis(-1);

        /**
         * commandTimeout
         */
        private int commandTimeout = 10000;

        /**
         * shutdownTimeout
         */
        private int shutdownTimeout = 10000;
    }
}
