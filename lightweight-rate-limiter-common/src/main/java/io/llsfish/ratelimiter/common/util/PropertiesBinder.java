package io.llsfish.ratelimiter.common.util;

import io.llsfish.ratelimiter.common.config.RtProperties;
import io.llsfish.ratelimiter.common.constant.Constant;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.ResolvableType;

import java.util.Map;

/**
 * @author Redick01
 */
public final class PropertiesBinder {

    private PropertiesBinder() { }

    /**
     * environment properties bind to {@link RtProperties}.
     *
     * @param properties environment properties
     * @param rtProperties {@link RtProperties}
     */
    public static void bindRtProperties(Map<?, Object> properties, RtProperties rtProperties) {
        ConfigurationPropertySource sources = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(sources);
        ResolvableType type = ResolvableType.forClass(RtProperties.class);
        Bindable<?> target = Bindable.of(type).withExistingValue(rtProperties);
        binder.bind(Constant.CONFIG_PREFIX, target);
    }
}
