package io.llsfish.ratelimiter.common.enums;

import lombok.Getter;

/**
 * @author Redick01
 */
@Getter
public enum RateLimitEnum {

    /**
     * 限流算法枚举
     */

    SLIDING_WINDOW("sliding_window_rate_limiter", "sliding_window_limiter.lua"),

    LEAKY_BUCKET("leaky_bucket_rate_limiter", "leaky_bucket_limiter.lua"),

    TOKEN_BUCKET("token_bucket_rate_limiter", "token_bucket_limiter.lua");

    /**
     * 算法名
     */
    private final String algorithmKey;

    /**
     * 脚本名
     */
    private final String scriptName;

    RateLimitEnum(final String algorithmKey, final String scriptName) {
        this.algorithmKey = algorithmKey;
        this.scriptName = scriptName;
    }
}
