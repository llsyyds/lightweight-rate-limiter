package io.llsfish.ratelimiter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Redick01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface RateLimiter {

    /**
     * return rate limit key.
     * @return rate limit key
     */
    String key();

    /**
     * rate limit callback class.
     * @return class
     */
    Class<?> clazz();
}
