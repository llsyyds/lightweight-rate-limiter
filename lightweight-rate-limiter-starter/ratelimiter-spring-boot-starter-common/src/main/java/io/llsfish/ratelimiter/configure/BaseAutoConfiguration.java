package io.llsfish.ratelimiter.configure;

import io.llsfish.ratelimiter.RateLimiterHandler;
import io.llsfish.ratelimiter.banner.RateLimiterBanner;
import io.llsfish.ratelimiter.monitor.Monitor;
import io.llsfish.ratelimiter.redis.RedisClientInitialization;
import io.llsfish.ratelimiter.registry.RateLimiterRegistry;
import io.llsfish.ratelimiter.aop.RateLimiterInterceptor;
import io.llsfish.ratelimiter.common.config.RtProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Redick01
 */
@Configuration
@EnableConfigurationProperties(RtProperties.class)
@EnableAspectJAutoProxy
public class BaseAutoConfiguration {

    /**
     * connectPoll init bean.
     * @param rtProperties {@link RtProperties}
     * @return rate
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisClientInitialization redisClientInitialization(RtProperties rtProperties) {
        return new RedisClientInitialization(rtProperties);
    }

    /**
     * rate limiter registry bean.
     * @param rtProperties {@link RtProperties}
     * @return RateLimiterRegistry
     */
    @Bean
    @ConditionalOnMissingBean
    public RateLimiterRegistry rateLimiterRegistry(RtProperties rtProperties) {
        return new RateLimiterRegistry(rtProperties);
    }

    /**
     * {@link RateLimiterHandler} bean.
     * @return RateLimiterHandler bean
     */
    @Bean
    @ConditionalOnMissingBean
    public RateLimiterHandler rateLimiterHandler() {
        return new RateLimiterHandler();
    }

    /**
     * {@link RateLimiterInterceptor} bean.
     * @return RateLimiterInterceptor
     */
    @Bean
    @ConditionalOnMissingBean
    public RateLimiterInterceptor rateLimiterInterceptor() {
        return new RateLimiterInterceptor();
    }

    /**
     * {@link RateLimiterBanner} bean.
     * @return RateLimiterBanner
     */
    @Bean
    public RateLimiterBanner rateLimiterBanner() {
        return new RateLimiterBanner();
    }

    /**
     * {@link Monitor} bean.
     * @return Monitor
     */
    @Bean
    @ConditionalOnMissingBean
    public Monitor monitor() {
        return new Monitor();
    }
}
