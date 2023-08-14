package io.llsfish.ratelimiter;

import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.llsfish.ratelimiter.algorithm.RateLimiterAlgorithm;
import io.llsfish.ratelimiter.common.config.RateLimiterConfigProperties;
import io.llsfish.ratelimiter.common.enums.Singleton;
import io.llsfish.ratelimiter.monitor.MetricsRegistry;
import io.llsfish.ratelimiter.parser.script.ScriptParser;
import io.llsfish.spi.ExtensionLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class RateLimiterHandler {
    public boolean isAllowed(final RateLimiterConfigProperties rateLimiterConfig, final Object[] args) {
    RateLimiterAlgorithm<?> rateLimiterAlgorithm = ExtensionLoader
            .getExtensionLoader(RateLimiterAlgorithm.class)
            .getJoin(rateLimiterConfig.getAlgorithmName());

    String redisScript = rateLimiterAlgorithm.getScript();
    ScriptParser parser = ExtensionLoader
            .getExtensionLoader(ScriptParser.class)
            .getJoin(rateLimiterConfig.getExpressionType());

    String realKey = parser.getExpressionValue(rateLimiterConfig.getRateLimiterKey(), args);
    List<String> keys = rateLimiterAlgorithm.getKeys(realKey);

    List<Long> result = null;

    StatefulRedisConnection<String, String> connection = null;
        // 使用 try-with-resources 语句确保连接被正确关闭
    try {
        connection = (StatefulRedisConnection<String, String>) Singleton.INST.get(GenericObjectPool.class).borrowObject();
        RedisCommands<String, String> syncCommands = connection.sync();
        Object eval = syncCommands.eval(
                redisScript,
                ScriptOutputType.MULTI,
                keys.toArray(new String[0]),
                doubleToString(rateLimiterConfig.getRate()),
                doubleToString(rateLimiterConfig.getCapacity()),
                doubleToString(Instant.now().getEpochSecond()),
                doubleToString(1.0));

        List<?> temp = (List<?>) eval;
        result = temp.stream()
                .map(obj -> (Long) obj)
                .collect(Collectors.toList());

        if (result == null) {
            log.error("Rate limiter execution returned null result. This is unexpected and might indicate a problem.");
            throw new RuntimeException("Rate limiter result is null");
        }
        
        Long tokensLeft = result.get(1);
        log.info("rate limiter core data: {}", tokensLeft);
        return result.get(0) == 1L;
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (connection!=null){
            Singleton.INST.get(GenericObjectPool.class).returnObject(connection);
        }
        List<Long> finalResult = result;
        new Thread(() -> {
            MetricsRegistry.refresh(rateLimiterConfig.getRateLimiterKey(), keys.get(0),
                finalResult, rateLimiterConfig);
        }).start();
    }
        return false;
    }


    private String doubleToString(final double param) {
        return String.valueOf(param);
    }
}
