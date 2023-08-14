package io.llsfish.ratelimiter.example.retelimit;

import io.llsfish.ratelimiter.callback.RateLimitCallback;

/**
 * @author Redick01
 */
public class RateLimiterResponse implements RateLimitCallback<String> {

    @Override
    public String rateLimitReturn(Object[] args, String rateLimitKey) {
        return "222";
    }
}
