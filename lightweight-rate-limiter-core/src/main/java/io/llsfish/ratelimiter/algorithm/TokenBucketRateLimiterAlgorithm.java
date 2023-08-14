package io.llsfish.ratelimiter.algorithm;

import io.llsfish.ratelimiter.common.enums.RateLimitEnum;
import io.llsfish.spi.Join;

/**
 * @author Redick01
 */
@Join
public class TokenBucketRateLimiterAlgorithm extends AbstractRateLimiterAlgorithm {

    public TokenBucketRateLimiterAlgorithm() {
        super(RateLimitEnum.TOKEN_BUCKET.getScriptName());
    }

    @Override
    protected String getKeyName() {
        return RateLimitEnum.TOKEN_BUCKET.getAlgorithmKey();
    }
}
