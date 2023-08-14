package io.llsfish.ratelimiter.monitor.collector;

import io.llsfish.ratelimiter.monitor.RateLimiterMetrics;

/**
 * @author Redick01
 */
public interface Collector {

    /**
     * collect metrics.
     *
     * @param appName application name
     * @param rateLimiterMetrics {@link RateLimiterMetrics}
     */
    void collect(String appName, RateLimiterMetrics rateLimiterMetrics);
}
