package io.llsfish.ratelimiter.algorithm;

import io.llsfish.ratelimiter.common.enums.RateLimitEnum;
import io.llsfish.spi.Join;

/**
 * @author Redick01
 */
@Join
public class LeakyBucketRateLimiterAlgorithm extends AbstractRateLimiterAlgorithm {

    public LeakyBucketRateLimiterAlgorithm() {
        super(RateLimitEnum.LEAKY_BUCKET.getScriptName());
    }

    @Override
    protected String getKeyName() {
        return RateLimitEnum.LEAKY_BUCKET.getAlgorithmKey();
    }
}
