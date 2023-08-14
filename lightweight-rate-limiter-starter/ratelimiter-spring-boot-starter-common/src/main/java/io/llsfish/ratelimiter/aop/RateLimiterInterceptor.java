package io.llsfish.ratelimiter.aop;

import io.llsfish.ratelimiter.RateLimiterHandler;
import io.llsfish.ratelimiter.registry.RateLimiterRegistry;
import io.llsfish.ratelimiter.annotation.RateLimiter;
import io.llsfish.ratelimiter.callback.RateLimitCallback;
import io.llsfish.ratelimiter.common.config.RateLimiterConfigProperties;
import io.llsfish.ratelimiter.common.enums.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;


/**
 * @author Redick01
 */
@Aspect
@Slf4j
public class RateLimiterInterceptor {

    @Resource
    private RateLimiterHandler rateLimiterHandler;

    /**
     * rate limiter pointcut.
     * 这个切入点指定了其关注的是带有@RateLimiter注解的方法
     */
    @Pointcut("@annotation(io.llsfish.ratelimiter.annotation.RateLimiter)")
    public void pointcut() { }

    /**
     * aop around.
     * @param joinPoint pointcut
     * @return is allowed
     * @throws Throwable {@link Throwable}
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Method method = getMethod(joinPoint);
            if (Objects.nonNull(method.getAnnotation(RateLimiter.class))) {
                String rateLimiterKey = method.getAnnotation(RateLimiter.class).key();
                if (StringUtils.isNotBlank(rateLimiterKey)) {
                    RateLimiterConfigProperties rateLimiterConfig =
                        RateLimiterRegistry.RATE_LIMITER_REGISTRY.get(rateLimiterKey);
                    if (Objects.nonNull(rateLimiterConfig)) {
                        if (!rateLimiterHandler.isAllowed(rateLimiterConfig, joinPoint.getArgs())) {
                            log.info("touch off rate limit !");
                            Class<?> clazz = method.getAnnotation(RateLimiter.class).clazz();
                            return rateLimitResponse(clazz, method, joinPoint.getArgs(), rateLimiterKey);
                        }
                    } else {
                        log.debug("RateLimiterConfigProperties config rateLimiterKey is null");
                    }
                }
            }
        } catch (Throwable e) {
            log.info("RateLimiter Exception", e);
        }
        return joinPoint.proceed();
    }

    /**
     * get {@link Method}.
     * @param joinPoint {@link ProceedingJoinPoint}
     * @return Method
     */
    private Method getMethod(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }

    /**
     * 执行限流回调方法
     * rate limiter call back
     * @param clazz {@link Class}
     * @param method {@link Method}
     * @param args parameter
     * @param key key
     * @return Object
     * @throws InstantiationException {@link InstantiationException}
     * @throws IllegalAccessException {@link IllegalAccessException}
     */
    private Object rateLimitResponse(Class<?> clazz, Method method, Object[] args, String key) throws
        InstantiationException, IllegalAccessException {
        RateLimitCallback<?> callback = (RateLimitCallback<?>) Singleton.INST.get(clazz);
        if (Objects.isNull(callback)) {
            callback = (RateLimitCallback<?>) clazz.newInstance();
            Singleton.INST.single(clazz, callback);
        }
        Object result = callback.rateLimitReturn(args, key);
        Class<?> returnClass = method.getReturnType();
        if (result.getClass() != returnClass
            && !returnClass.isAssignableFrom(result.getClass())) {
            String msg = String.format("wrong return type of method. methodName: [%s] required: [%s]"
                + ", input: [%s]", method.getName(), returnClass, result.getClass());
            throw new RuntimeException(msg);
        }
        return result;
    }
}
