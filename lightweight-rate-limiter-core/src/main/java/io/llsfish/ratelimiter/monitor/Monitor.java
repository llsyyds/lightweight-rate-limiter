package io.llsfish.ratelimiter.monitor;

import cn.hutool.core.thread.NamedThreadFactory;
import io.lettuce.core.api.StatefulRedisConnection;
import io.llsfish.ratelimiter.common.config.RtProperties;
import io.llsfish.ratelimiter.common.enums.Singleton;
import io.llsfish.ratelimiter.monitor.collector.Collector;
import io.llsfish.ratelimiter.monitor.collector.MetricsCollector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Redick01
 */
@Slf4j
public class Monitor implements ApplicationRunner, Ordered {

    private static final ScheduledExecutorService MONITOR_EXECUTOR = new ScheduledThreadPoolExecutor(
        1, new NamedThreadFactory("rate-limiter-monitor", true));

    private static final ScheduledExecutorService RECOVER_TOKEN_EXECUTOR = new ScheduledThreadPoolExecutor(
        1, new NamedThreadFactory("recover-token-executor", true));

    @Value("${spring.application.name}")
    private String appName;

    @Resource
    private RtProperties rtProperties;

    public Monitor() {
        Singleton.INST.single(Collector.class, new MetricsCollector());
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MONITOR_EXECUTOR.scheduleWithFixedDelay(this::run,
                0, rtProperties.getMonitorInterval(), TimeUnit.SECONDS);

        RECOVER_TOKEN_EXECUTOR.scheduleWithFixedDelay(this::recover,
                0, rtProperties.getRecoverInterval(), TimeUnit.SECONDS);
    }

    /**
     * collect
     */
    private void run() {
        if (rtProperties.getEnableMonitor()) {
            log.info("rate limiter metrics begin.");
            Map<String, RateLimiterMetrics> registry = MetricsRegistry.METRICS_MAP;
            registry.forEach((k, v) -> {
                Singleton.INST.get(Collector.class).collect(appName, v);
            });
        }
    }

    /**
     * recover
     */
    private void recover() {
        if (rtProperties.getEnableMonitor()) {
            log.info("recover the rate limiter metrics tokens left.");
            Map<String, RateLimiterMetrics> registry = MetricsRegistry.METRICS_MAP;
            registry.forEach((k, v) -> {
                StatefulRedisConnection<String, String> connection = null;
                try {
                    connection = (StatefulRedisConnection<String, String>) Singleton.INST.get(GenericObjectPool.class).borrowObject();
                    String res = connection.sync().get(v.getRealKey());
                    /**
                     *  如果redis中有这个realkey的话，证明刚刚有用户请求过后端，这时候就不用执行这个逻辑，
                     *  让grafana展示那次请求的数据，过一段时间后，这个realkey没了，而且执行到这了就证明很久没有请求过来了，
                     *  得重置一下rateLimitermetrics数据状态，让grafana面板展示数据比较好看
                     */
                    if (null == res) {
                        MetricsRegistry.recover(k,v.getCapacity().longValue());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (connection!=null){
                        Singleton.INST.get(GenericObjectPool.class).returnObject(connection);
                    }
                }
            });
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
