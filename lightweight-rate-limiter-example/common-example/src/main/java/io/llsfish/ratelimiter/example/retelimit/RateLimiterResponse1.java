package io.llsfish.ratelimiter.example.retelimit;

import io.llsfish.ratelimiter.callback.RateLimitCallback;
import io.llsfish.ratelimiter.example.dto.Response;

/**
 * @author Redick01
 */
public class RateLimiterResponse1 implements RateLimitCallback<Response<String>> {

    @Override
    public Response<String> rateLimitReturn(Object[] args, String rateLimitKey) {
        return new Response<>("998", "限流", "rate limit");
    }
}
