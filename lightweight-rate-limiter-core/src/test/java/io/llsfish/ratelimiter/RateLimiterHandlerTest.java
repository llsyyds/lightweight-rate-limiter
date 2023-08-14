package io.llsfish.ratelimiter;

import com.google.common.collect.Lists;
import io.llsfish.ratelimiter.common.config.RateLimiterConfigProperties;
import io.llsfish.ratelimiter.common.config.RtProperties;
import io.llsfish.ratelimiter.redis.RedisClientInitialization;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Redick01
 */
public class RateLimiterHandlerTest {

    private RtProperties rtProperties;

    private RedisClientInitialization redisClientInitialization;

    private RateLimiterHandler rateLimiterHandler;

    @BeforeEach
    public void startup() throws Exception {
        rtProperties = new RtProperties();
        RtProperties.RedisConfig redisConfig = new RtProperties.RedisConfig();
        redisConfig.setDatabase(0);
        redisConfig.setUrl("121.4.219.216");
        redisConfig.setPassword("gzpu44ry");
        rtProperties.setRedisConfig(redisConfig);
        RateLimiterConfigProperties rateLimiterConfig = new RateLimiterConfigProperties();
        List<RateLimiterConfigProperties> limiterConfigs = Lists.newArrayList();
        rateLimiterConfig.setRateLimiterKey("redisTemplateInitialization");
        rateLimiterConfig.setRate(1d);
        rateLimiterConfig.setAlgorithmName("token_bucket_rate_limiter");
        rateLimiterConfig.setCapacity(1000d);
        limiterConfigs.add(rateLimiterConfig);
        rtProperties.setRateLimiterConfigs(limiterConfigs);
        redisClientInitialization = new RedisClientInitialization(rtProperties);
        rateLimiterHandler = new RateLimiterHandler();
    }

    @Test
    public void isAllowed() {
        Assertions.assertTrue(rateLimiterHandler.isAllowed(rtProperties.getRateLimiterConfigs().get(0), null));
    }
}
