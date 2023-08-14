package io.llsfish.ratelimiter.common.enums;

import lombok.Getter;

/**
 * @author Redick01
 */
@Getter
public enum ConfigFileTypeEnum {

    /**
     * Config file type.
     */
    PROPERTIES("properties"),
    XML("xml"),
    JSON("json"),
    YML("yml"),
    YAML("yaml");

    private final String value;

    ConfigFileTypeEnum(String value) {
        this.value = value;
    }

    /**
     * enum config type.
     *
     * @param value config value
     * @return config enum
     */
    public static ConfigFileTypeEnum of(String value) {
        for (ConfigFileTypeEnum typeEnum : ConfigFileTypeEnum.values()) {
            if (typeEnum.value.equals(value)) {
                return typeEnum;
            }
        }
        return PROPERTIES;
    }
}
