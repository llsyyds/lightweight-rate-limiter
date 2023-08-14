package io.llsfish.ratelimiter.monitor;

import lombok.Builder;
import lombok.Data;

/**
 * @author Redick01
 */
@Data
@Builder
public class RateLimiterMetrics {

    private String algorithmName;

    private String realKey;

    private Double capacity;

    private Double rate;

    private Long tokensLeft;

    private CountHolder rejectCount;
}
