package io.llsfish.ratelimiter.refresh;

import io.llsfish.ratelimiter.common.enums.ConfigFileTypeEnum;

/**
 * @author Redick01
 */
public interface Refresher {

    /**
     * refresh rate limiter config.
     * @param content config
     * @param fileType config file type
     */
    void refresh(String content, ConfigFileTypeEnum fileType);
}
