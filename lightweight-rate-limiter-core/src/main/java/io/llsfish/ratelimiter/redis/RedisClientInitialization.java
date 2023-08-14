package io.llsfish.ratelimiter.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.support.ConnectionPoolSupport;
import io.llsfish.ratelimiter.common.config.RtProperties;
import io.llsfish.ratelimiter.common.enums.Singleton;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;
import java.util.Objects;

/**
 * @PROJECT_NAME: lightweight-rate-limiter
 * @DESCRIPTION:
 * @AUTHOR: lls
 * @DATE: 2023/8/12 13:22
 */

public class RedisClientInitialization {
    private final GenericObjectPool<StatefulRedisConnection<String, String>> connectionPool;

    public RedisClientInitialization(RtProperties rtProperties) {
        //如果rtProperties为空，则返回自定义异常
        RtProperties.RedisConfig redisConfig = Objects.requireNonNull(rtProperties,"redisconfig must not be null").getRedisConfig();

        // 创建 Lettuce 的 ClientResources（提供了一组默认的客户端资源（配置））
        ClientResources clientResources = DefaultClientResources.create();

        RedisURI.Builder redisURIBuilder = RedisURI.builder()
                .withHost(redisConfig.getUrl())
                .withPassword(redisConfig.getPassword().toCharArray())
                .withTimeout(Duration.ofMillis(redisConfig.getCommandTimeout()));;

        // 创建 RedisClient
        RedisClient redisClient = RedisClient.create(clientResources, redisURIBuilder.build());

        // 创建连接池配置
        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(redisConfig.getMaxIdle());
        poolConfig.setMinIdle(redisConfig.getMinIdle());
        poolConfig.setMaxTotal(redisConfig.getMaxActive());

        // 使用 Lettuce 提供的 ConnectionPoolSupport 来创建连接池
        connectionPool = ConnectionPoolSupport.createGenericObjectPool(redisClient::connect, poolConfig);

        if (Objects.isNull(Singleton.INST.get(GenericObjectPool.class))) {
            Singleton.INST.single(GenericObjectPool.class, connectionPool);
        }
    }
}

