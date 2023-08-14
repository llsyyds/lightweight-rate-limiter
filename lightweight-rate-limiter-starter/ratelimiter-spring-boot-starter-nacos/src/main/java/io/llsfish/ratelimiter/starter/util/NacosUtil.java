package io.llsfish.ratelimiter.starter.util;

import io.llsfish.ratelimiter.common.config.RtProperties;
import io.llsfish.ratelimiter.common.enums.ConfigFileTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @author Redick01
 */
public class NacosUtil {

    /**
     * get nacos config dataId.
     * @param nacos nacos config
     * @param environment {@link Environment}
     * @param configFileTypeEnum {@link ConfigFileTypeEnum}
     * @return nacos config dataId
     */
    public static String getDataId(RtProperties.Nacos nacos, Environment environment,
        ConfigFileTypeEnum configFileTypeEnum) {
        String dataId = "";
        if (Objects.nonNull(nacos) && StringUtils.isNotBlank(nacos.getDataId())) {
            dataId = nacos.getDataId();
        } else {
            String[] profiles = environment.getActiveProfiles();
            if (profiles.length < 1) {
                profiles = environment.getDefaultProfiles();
            }
            String appName = environment.getProperty("spring.application.name");
            appName = StringUtils.isNoneBlank(appName) ? appName : "application";
            dataId = appName + "-" + profiles[0] + "." + configFileTypeEnum.getValue();
        }
        return dataId;
    }

    /**
     * get nacos group.
     * @param nacos nacos config info
     * @return nacos group
     */
    public static String getGroup(RtProperties.Nacos nacos) {
        String group = "DEFAULT_GROUP";
        if (nacos != null && StringUtils.isNotBlank(nacos.getGroup())) {
            group = nacos.getGroup();
        }
        return group;
    }

    /**
     * {@link ConfigFileTypeEnum}.
     * @param rtProperties {@link RtProperties}
     * @return ConfigFileTypeEnum
     */
    public static ConfigFileTypeEnum getConfigFileType(RtProperties rtProperties) {
        ConfigFileTypeEnum configFileType = ConfigFileTypeEnum.PROPERTIES;
        if (StringUtils.isNotBlank(rtProperties.getConfigType())) {
            configFileType = ConfigFileTypeEnum.of(rtProperties.getConfigType());
        }
        return configFileType;
    }
}
