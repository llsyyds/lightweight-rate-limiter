package io.llsfish.ratelimiter.refresh;

import cn.hutool.core.map.MapUtil;
import io.llsfish.ratelimiter.common.config.RtProperties;
import io.llsfish.ratelimiter.common.enums.ConfigFileTypeEnum;
import io.llsfish.ratelimiter.common.util.PropertiesBinder;
import io.llsfish.ratelimiter.parser.config.ConfigParser;
import io.llsfish.ratelimiter.registry.RateLimiterRegistry;
import io.llsfish.spi.ExtensionLoader;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * @author Redick01
 */
@Slf4j
public abstract class AbstractRefresher implements Refresher {

    @Resource
    private RtProperties rtProperties;

    @Override
    public void refresh(final String content, final ConfigFileTypeEnum fileType) {
        try {
            ConfigParser configParser = ExtensionLoader.getExtensionLoader(ConfigParser.class)
                .getJoin(fileType.getValue());
            Map<Object, Object> properties = configParser.doParse(content);
            doRefresh(properties);
        } catch (Exception e) {
            log.error("parse config content Exception");
            throw new RuntimeException(e);
        }
    }

    /**
     * do refresh rate limiter container.
     * @param rtProperties {@link RtProperties}
     */
    public void doRefresh(final RtProperties rtProperties) {
        if (Objects.isNull(rtProperties)) {
            log.error("config properties is empty, rate limiter refresh failed.");
        }
        RateLimiterRegistry.refresh(rtProperties);
    }

    /**
     * refresh rate limiter registry.
     * @param properties properties
     */
    protected void doRefresh(final Map<Object, Object> properties) {
        if (MapUtil.isEmpty(properties)) {
            log.error("config properties is empty, rate limiter refresh failed.");
            return;
        }
        PropertiesBinder.bindRtProperties(properties, rtProperties);
        RateLimiterRegistry.refresh(rtProperties);
    }
}
