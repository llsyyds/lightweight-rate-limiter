package io.llsfish.ratelimiter.callback;

/**
 * @author Redick01
 */
public interface RateLimitCallback<T> {

    /**
     * rate limiter call back.
     * @param args parameter
     * @param rateLimitKey rate limiter key
     * @return response
     */
    T rateLimitReturn(Object[] args, String rateLimitKey);
}
