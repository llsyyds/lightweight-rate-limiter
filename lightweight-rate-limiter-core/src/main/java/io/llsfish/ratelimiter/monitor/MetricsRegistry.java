package io.llsfish.ratelimiter.monitor;

import com.google.common.collect.Maps;
import io.llsfish.ratelimiter.common.config.RateLimiterConfigProperties;

import java.util.List;
import java.util.Map;

/**
 * @author Redick01
 */
public final class MetricsRegistry {

    private MetricsRegistry() { }

    public static final Map<String, RateLimiterMetrics> METRICS_MAP = Maps.newConcurrentMap();

    /**
     * refresh metrics registry
     * @param key key
     * @param realKey real key
     * @param tokensLeft tokens left
     * @param properties {@link RateLimiterConfigProperties}
     */
    public static void refresh(final String key, final String realKey, final List<Long> tokensLeft,
        final RateLimiterConfigProperties properties) {
        if (METRICS_MAP.containsKey(key)) {
            RateLimiterMetrics metrics = METRICS_MAP.get(key);
            metrics.setRealKey(realKey);
            metrics.setTokensLeft(tokensLeft.get(1));
            metrics.setCapacity(properties.getCapacity());
            metrics.setRate(properties.getRate());
            metrics.setAlgorithmName(properties.getAlgorithmName());
            if (tokensLeft.get(0) != 1L) {
                metrics.getRejectCount().add();
            }
        } else {
            RateLimiterMetrics metrics = RateLimiterMetrics.builder()
                    .realKey(realKey)
                    .capacity(properties.getCapacity())
                    .rate(properties.getRate())
                    .algorithmName(properties.getAlgorithmName())
                    .tokensLeft(tokensLeft.get(1))
                    .rejectCount(new CountHolder())
                    .build();
            METRICS_MAP.put(key, metrics);
        }
    }

    /**
     * 定时更新rateLimitermetrics的数据，因为如果没有发生请求到后端的话，rateLimitermetrics会维持上一次请求后的数据状态
     * 这样在grafana数据展示出来不好看。
     * recover metrics rate limiter.
     * @param key key
     * @param
     */
    public static void recover(final String key, final Long capacity) {
        if (METRICS_MAP.containsKey(key)) {
            RateLimiterMetrics metrics = METRICS_MAP.get(key);
            metrics.setTokensLeft(capacity);
        }
    }
}
